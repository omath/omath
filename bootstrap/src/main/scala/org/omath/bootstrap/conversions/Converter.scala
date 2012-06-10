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
      case ConvertableToExpression(y) => y
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

  def fromExpression(x: Expression, `type`: Type): Option[Object] = fromExpression(x, `type`.toString.stripPrefix("class ").stripPrefix("interface "))

  def fromExpression(x: Expression, `type`: String): Option[Object] = {
    info("trying to convert " + x + " to an instance of " + `type`)
    ((x, `type`) match {
      case (x: SymbolExpression, "org.omath.SymbolExpression") => Some(x)
      case (x: IntegerExpression, "org.omath.IntegerExpression") => Some(x)
      case (x: RealExpression, "org.omath.RealExpression") => Some(x)
      case (x: StringExpression, "org.omath.StringExpression") => Some(x)
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
      case (x: StringExpression, "java.lang.String") => Some(x.contents)
      case (FullFormExpression(org.omath.symbols.List, arguments), SeqPattern(innerType)) => {
        arguments.map(fromExpression(_, innerType)) match {
          case options if options.forall(_.nonEmpty) => Some(options.map(_.get))
          case _ => None
        }
      }
      case ConvertableToInstance(i) => Some(i)
      case _ => None
    }).map({ a =>
      info("success!")
      a.asInstanceOf[Object]
    })
  }

  private var toExpressionFunction: PartialFunction[Any, Expression] = PartialFunction.empty
  private var toInstanceFunction: PartialFunction[(Expression, String), Any] = PartialFunction.empty

  def registerConversionToExpression(f: PartialFunction[Any, Expression]) {
    toExpressionFunction = toExpressionFunction.orElse(f)
  }
  def registerConversionToInstance(f: PartialFunction[(Expression, String), Any]) {
    toInstanceFunction = toInstanceFunction.orElse(f)
  }

  private object ConvertableToInstance {
    def unapply[T](p: (Expression, String)): Option[T] = toInstanceFunction.lift(p).map(_.asInstanceOf[T])
  }

  private object ConvertableToExpression {
    def unapply(x: Any): Option[Expression] = toExpressionFunction.lift(x)
  }

}