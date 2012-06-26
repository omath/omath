package org.omath.core.arithmetic

import net.tqft.toolkit.algebra.EuclideanDomain
import org.omath.IntegerExpression

object IntegerExpressionArithmetic extends EuclideanDomain[IntegerExpression] {
  override def fromInt(x: Int): IntegerExpression = IntegerExpression(x)
  override val zero = IntegerExpression(0)
  override val one = IntegerExpression(1)
  override def add(x: IntegerExpression, y: IntegerExpression) = IntegerExpression(x.toApint.add(y.toApint))
  override def multiply(x: IntegerExpression, y: IntegerExpression) = IntegerExpression(x.toApint.multiply(y.toApint))
  override def negate(x: IntegerExpression) = IntegerExpression(x.toApint.negate)
  override def quotientRemainder(x: IntegerExpression, y: IntegerExpression) = (IntegerExpression(x.toApint.divide(y.toApint)), IntegerExpression(x.toApint.mod(y.toApint)))
  
  // TODO lift to toolkit-algebra
  def addOption(x: Seq[IntegerExpression]) = if(x.size == 0) None else Some(add(x))
  def multiplyOption(x: Seq[IntegerExpression]) = if(x.size == 0) None else Some(multiply(x))
}