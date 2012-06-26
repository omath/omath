package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class OptionalPattern(val inner: Pattern, val default: Expression) extends Pattern {
  override def asExpression = symbols.Optional(inner.asExpression, default)
  override def pure = false
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    inner.extend(a) ++ inner.extend(a.binding)(default).map(m => a.copy(binding = m, lastBound = Seq(default)))
  }
  override def names = inner.names
}

