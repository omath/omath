package org.omath.bootstrap.conversions

import net.tqft.toolkit.Logging
import org.omath.Expression
import org.omath.IntegerExpression
import org.omath.RealExpression
import org.omath.StringExpression
import org.omath.bootstrap.JavaObjectExpression
import org.omath.kernel.Evaluation
import org.omath.patterns.Pattern
import scala.math.BigDecimal.double2bigDecimal
import org.apfloat.Apint
import org.apfloat.Apfloat

object Converter extends Logging {

  def toExpression(x: Any): Expression = {
    x match {
      case x: Int => IntegerExpression(x)
      case x: Long => IntegerExpression(x)
      case x: BigInt => IntegerExpression(x)
      case x: Apint => IntegerExpression(x)
      case x: String => StringExpression(x)
      case x: Float => RealExpression(x)
      case x: Double => RealExpression(x)
      case x: BigDecimal => RealExpression(x)
      case x: Apfloat => RealExpression(x)
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

  def fromExpression[T](x: Expression, clazz: Class[T]): Option[T] = {
    info("trying to convert " + x + " to an instance of " + clazz.getName)
    (x, clazz.getName) match {
      case (x: IntegerExpression, "int") => Some(x.toInt.asInstanceOf[T])
      case (x: IntegerExpression, "long") => Some(x.toLong.asInstanceOf[T])
      case (x: IntegerExpression, "java.lang.Int") => Some(x.toInt.asInstanceOf[T])
      case (x: IntegerExpression, "java.lang.Long") => Some(x.toLong.asInstanceOf[T])
      case (x: IntegerExpression, "org.apfloat.Apint") => Some(x.toApint.asInstanceOf[T])
      case (x: RealExpression, "java.lang.Float") => Some(x.toFloat.asInstanceOf[T])
      case (x: RealExpression, "java.lang.Double") => Some(x.toDouble.asInstanceOf[T])
      case (x: RealExpression, "float") => Some(x.toFloat.asInstanceOf[T])
      case (x: RealExpression, "double") => Some(x.toDouble.asInstanceOf[T])
      case (x: RealExpression, "org.apfloat.Apfloat") => Some(x.toApfloat.asInstanceOf[T])
      case (x: StringExpression, "java.lang.String") => Some(x.contents.asInstanceOf[T])
      case ConvertableToInstance(i) => Some(i)
      case _ => None
    }
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