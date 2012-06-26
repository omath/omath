package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

trait GenericRepeated extends Pattern {
  def inner: Pattern
  override def pure = inner.pure
  override def names = inner.names
}

private case class RepeatedPattern(override val inner: Pattern) extends GenericRepeated {
  override def asExpression = symbols.Repeated(inner.asExpression)
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

private case class RepeatedNullPattern(override val inner: Pattern) extends GenericRepeated {
  override def asExpression = symbols.RepeatedNull(inner.asExpression)
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