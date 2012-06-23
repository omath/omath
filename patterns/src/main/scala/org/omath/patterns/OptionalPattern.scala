package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class OptionalPattern(val inner: ExpressionPattern, val default: Expression) extends ExpressionPattern {
  override def expression = symbols.Optional(inner.expression, default)
  override def pure = false
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    inner.extend(a) ++ inner.extend(a.binding)(default).map(m => a.copy(binding = m, lastBound = Seq(default)))
  }
}

