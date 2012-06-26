package org.omath.patterns

import org.omath.kernel.Evaluation

case class HoldPattern(inner: Pattern) extends Pattern {
  override def asExpression = org.omath.symbols.HoldPattern(inner.asExpression)
  override def pure = inner.pure
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = inner.extend(a)
  override def names = inner.names
}