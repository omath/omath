package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class RepeatedPattern(inner: ExpressionPattern) extends ExpressionPattern {
  override def pure = inner.pure
  override def expression = symbols.Repeated(inner.expression)
  private val nullCompanion = RepeatedNullPattern(inner)
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    if (a.remainingExpressions.isEmpty) {
      Iterator.empty
    } else {
      for (
        b <- inner.extend(a);
        c <- nullCompanion.extend(b)
      ) yield {
        c.copy(lastBound = b.lastBound ++ c.lastBound)
      }
    }
  }
}

private case class RepeatedNullPattern(inner: ExpressionPattern) extends ExpressionPattern {
  override def pure = inner.pure
  override def expression = symbols.RepeatedNull(inner.expression)
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    if (a.remainingExpressions.isEmpty) {
      Iterator(a.copy(lastBound = Seq.empty))
    } else {
      for (
        b <- inner.extend(a);
        c <- this.extend(b)
      ) yield {
        c.copy(lastBound = b.lastBound ++ c.lastBound)
      }
    }
  }
}