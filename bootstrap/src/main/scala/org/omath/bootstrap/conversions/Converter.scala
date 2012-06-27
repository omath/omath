package org.omath.bootstrap.conversions

import net.tqft.toolkit.Logging
import org.omath._
import org.omath.bootstrap.JavaObjectExpression
import org.omath.kernel.Evaluation
import org.omath.patterns.Pattern
import scala.math.BigDecimal.double2bigDecimal
import org.apfloat.Apint
import org.apfloat.Apfloat
import java.lang.reflect.Type
import org.omath.patterns.ReplacementRuleTable
import org.omath.patterns.ReplacementRule

object Converter extends Logging {

  def toExpression(x: Any): Expression = {
    x match {
      case x: Expression => x
      case null => symbols.Null
      case true => org.omath.symbols.True
      case false => org.omath.symbols.False
      case x: Int => IntegerExpression(x)
      case x: Long => IntegerExpression(x)
      case x: BigInt => IntegerExpression(x)
      case x: Apint => IntegerExpression(x)
      case x: String => StringExpression(x)
      case x: Float => RealExpression(x)
      case x: Double => RealExpression(x)
      case x: BigDecimal => RealExpression(x)
      case x: Apfloat => RealExpression(x)
      case x: Seq[_] => org.omath.symbols.List(x.map(toExpression): _*)
      case x: ReplacementRuleTable => toExpression(x.table)
      case x: ReplacementRule => x.asExpression
      case x: Context => StringExpression(x.toString)
      case ConvertableToExpression(y) => y // FIXME this is probably broken.
      case _ => JavaObjectExpression(x)
    }
  }
  def toExpressionMatching(x: Any, pattern: Pattern)(implicit evaluation: Evaluation): Option[Expression] = {
    val e = toExpression(x)
    if (pattern.matching(e).nonEmpty) {
      Some(e)
    } else {
      None
    }
  }

  private val SeqPattern = """scala.collection.Seq<(.*)>""".r

  def fromExpression(x: Expression, `type`: Type)(implicit evaluation: Evaluation): Option[Object] = fromExpression(x, `type`.toString.stripPrefix("class ").stripPrefix("interface "))

  def fromExpression(x: Expression, `type`: String)(implicit evaluation: Evaluation): Option[Object] = {
    implicit val attributes = evaluation.kernel.kernelState.attributes _
    
    def short(s: String) = {
      if(s.size > 200) {
        s.take(200) + "..."
      } else {
        s
      }
    }
    
    info("trying to convert " + short(x.toString) + " to an instance of " + `type`)
    ((x, `type`) match {
      case (x: SymbolExpression, "org.omath.SymbolExpression") => Some(x)
      case (x: IntegerExpression, "org.omath.IntegerExpression") => Some(x)
      case (x: RealExpression, "org.omath.RealExpression") => Some(x)
      case (x: StringExpression, "org.omath.StringExpression") => Some(x)
      case (x: FullFormExpression, "org.omath.FullFormExpression") => Some(x)
      case (x: Expression, "org.omath.Expression") => Some(x)
      case (x: IntegerExpression, "int") => Some(x.toInt)
      case (x: IntegerExpression, "long") => Some(x.toLong)
      case (x: IntegerExpression, "java.lang.Int") => Some(x.toInt)
      case (x: IntegerExpression, "java.lang.Long") => Some(x.toLong)
      case (x: IntegerExpression, "org.apfloat.Apint") => Some(x.toApint)
      case (x: RealExpression, "float") => Some(x.toFloat)
      case (x: RealExpression, "double") => Some(x.toDouble)
      case (x: RealExpression, "java.lang.Float") => Some(x.toFloat)
      case (x: RealExpression, "java.lang.Double") => Some(x.toDouble)
      case (x: RealExpression, "org.apfloat.Apfloat") => Some(x.toApfloat)
      case (symbols.True, "boolean") => Some(true)
      case (symbols.False, "boolean") => Some(false)
      case (x: StringExpression, "java.lang.String") => Some(x.contents)
      case (x: StringExpression, "org.omath.Context") => Some(Context(x.contents))
      case (x: Expression, "org.omath.patterns.Pattern") => Some({ val p: Pattern = x; p })
      case (symbols.Rule(lhs, rhs), "org.omath.patterns.ReplacementRule") => Some(lhs :> rhs)
      case (symbols.RuleDelayed(lhs, rhs), "org.omath.patterns.ReplacementRule") => Some(lhs :> rhs)
      case (symbols.Rule(lhs, rhs), "org.omath.patterns.ReplacementRuleTable") => Some(ReplacementRuleTable.singletonTable(lhs :> rhs))
      case (symbols.RuleDelayed(lhs, rhs), "org.omath.patterns.ReplacementRuleTable") => Some(ReplacementRuleTable.singletonTable(lhs :> rhs))
      case (FullFormExpression(symbols.List, arguments), "org.omath.patterns.ReplacementRuleTable") => {
        val lifted = arguments.map(a => fromExpression(a, "org.omath.patterns.ReplacementRule"))
        if(lifted.forall(_.nonEmpty)) {
          Some(ReplacementRuleTable(lifted.map(_.get.asInstanceOf[ReplacementRule])))
        } else {
          None
        }
      }
      case (x: JavaObjectExpression[_], `type`) if Class.forName(`type`).isAssignableFrom(x.contents.getClass) => {
        Some(x.contents)
      }
      case (FullFormExpression(_, arguments), SeqPattern(innerType)) => {
        arguments.map(fromExpression(_, innerType)) match {
          case options if options.forall(_.nonEmpty) => Some(options.map(_.get))
          case _ => None
        }
      }
      case p => toInstanceFunction.lift(p)
    }).map({ a =>
      info("success!")
      a.asInstanceOf[Object]
    })
  }

  private var toExpressionFunction: PartialFunction[Any, Expression] = Map.empty // PartialFunction.empty isn't available in Scala 2.9
  private var toInstanceFunction: PartialFunction[(Expression, String), Any] = Map.empty

  def registerConversionToExpression(f: PartialFunction[Any, Expression]) {
    toExpressionFunction = toExpressionFunction.orElse(f)
  }
  def registerConversionToInstance(f: PartialFunction[(Expression, String), Any]) {
    toInstanceFunction = toInstanceFunction.orElse(f)
  }

  registerConversionToInstance({ case (x: JavaObjectExpression[_], "org.omath.bootstrap.JavaObjectExpression<?>") => x })
  
  private object ConvertableToExpression {
    def unapply(x: Any): Option[Expression] = toExpressionFunction.lift(x)
  }

}