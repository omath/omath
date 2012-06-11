package org.omath.patterns

import org.omath.kernel.Evaluation

case class HoldPattern(inner: ExpressionPattern) extends ExpressionPattern {
  override def expression = org.omath.symbols.HoldPattern(inner.expression)
  override def pure = inner.pure
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = inner.extend(a)
}