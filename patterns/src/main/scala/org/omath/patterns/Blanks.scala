package org.omath.patterns

import org.omath._

private case class Blank(head: Option[SymbolExpression]) extends ExpressionPattern {
  override def pure = true
  override val expression = symbols.Blank(head.toSeq: _*)
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    // TODO once there's head/tail extractor for Seq, clean this up.
    (if (a.remainingExpressions.isEmpty || (head.nonEmpty && a.remainingExpressions.head.head != head.get)) {
      None
    } else {
      Some(a.copy(remainingExpressions = a.remainingExpressions.tail, lastBound = Seq(a.remainingExpressions.head)))
    }).iterator
  }
}
private case class BlankSequence(head: Option[SymbolExpression]) extends ExpressionPattern {
  override def pure = true
  override val expression = symbols.BlankSequence(head.toSeq: _*)
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    if (a.remainingExpressions.isEmpty || (head.nonEmpty && a.remainingExpressions.head.head != head.get)) {
      Iterator.empty
    } else {
      val range = head match {
        case None => (1 to a.remainingExpressions.size).iterator
        case Some(h) => (1 to a.remainingExpressions.size).iterator.takeWhile(i => a.remainingExpressions(i).head == h)
      }
      for (i <- range) yield PartialBinding(a.binding, a.remainingExpressions.drop(i), a.remainingExpressions.take(i))
    }
  }
}
private case class BlankNullSequence(head: Option[SymbolExpression]) extends ExpressionPattern {
  override def pure = true
  override val expression = symbols.BlankNullSequence(head.toSeq: _*)
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    val range = head match {
      case None => (0 to a.remainingExpressions.size).iterator
      case Some(h) => (0 to a.remainingExpressions.size).iterator.takeWhile(i => a.remainingExpressions(i).head == h)
    }
    for (i <- range) yield PartialBinding(a.binding, a.remainingExpressions.drop(i), a.remainingExpressions.take(i))
  }
}
