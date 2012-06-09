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
    if(pattern.bind(e).nonEmpty) {
      Some(e)
    } else {
      None
    }
  }
  
  def fromExpression[T](x: Expression, clazz: Class[T]): Option[T] = {
    info("trying to convert " + x + " to an instance of " + clazz.getName)
    (x, clazz.getName) match {
      case (x: IntegerExpression, "java.lang.Int") => Some(x.toInt.asInstanceOf[T])
      // TODO many more
      case ConvertableToInstance(i) => Some(i)
      case _ => None
    }
  }
  
  // TODO provide a way to register conversions, and have ConvertableX try these.
  
  private object ConvertableToInstance {
    def unapply[T](p: (Expression, String)): Option[T] = {
      val x = p._1
      val className = p._2
      None
    }
  }
  
  private object ConvertableToExpression {
    def unapply(x: Any): Option[Expression] = {
      None
    }
  }
  
}