package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation
import org.omath.util.Scala29Compatibility.+:

sealed trait PatternLength extends Ordered[PatternLength] {
  override def compare(other: PatternLength) = {
    (this, other) match {
      case (One, One) => 0
      case (One, OneOrMore) => -1
      case (One, ZeroOrMore) => -1
      case (OneOrMore, One) => 1
      case (OneOrMore, OneOrMore) => 0
      case (OneOrMore, ZeroOrMore) => -1
      case (ZeroOrMore, One) => 1
      case (ZeroOrMore, OneOrMore) => 1
      case (ZeroOrMore, ZeroOrMore) => 0
    }
  }
}
case object One extends PatternLength
case object OneOrMore extends PatternLength
case object ZeroOrMore extends PatternLength

object HeadComparator extends PartialOrdering[Option[SymbolExpression]] {
  override def lteq(x: Option[SymbolExpression], y: Option[SymbolExpression]) = {
    tryCompare(x, y).map(_ <= 0).getOrElse(false)
  }
  override def tryCompare(a: Option[SymbolExpression], b: Option[SymbolExpression]): Option[Int] = {
    (a, b) match {
      case (None, None) => Some(0)
      case (None, Some(h)) => Some(1)
      case (Some(h), None) => Some(-1)
      case (Some(ha), Some(hb)) => if (ha == hb) Some(0) else None
    }
  }
}

trait GenericBlank extends ExpressionPattern {
  override def pure = true
  def head: Option[SymbolExpression]
  def length: PatternLength
}

private case class Blank(override val head: Option[SymbolExpression]) extends GenericBlank {
  override val expression = symbols.Blank(head.toSeq: _*)
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    (a.remainingExpressions match {
      case h +: t => {
        head match {
          case Some(s) if h.head != s => None
          case _ => Some(PartialBinding(a.binding, a.remainingExpressions.tail, Seq(a.remainingExpressions.head)))
        }
      }
      case _ => None
    }).iterator
  }
  override val length = One
}

private case class BlankSequence(override val head: Option[SymbolExpression]) extends GenericBlank {
  override def pure = true
  override val expression = symbols.BlankSequence(head.toSeq: _*)
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    if (a.remainingExpressions.isEmpty || (head.nonEmpty && a.remainingExpressions.head.head != head.get)) {
      Iterator.empty
    } else {
      val range = head match {
        case None => (1 to a.remainingExpressions.size).iterator
        case Some(h) => (1 to a.remainingExpressions.size).iterator.takeWhile(i => a.remainingExpressions(i - 1).head == h)
      }
      for (i <- range) yield PartialBinding(a.binding, a.remainingExpressions.drop(i), a.remainingExpressions.take(i))
    }
  }
  override val length = OneOrMore
}

private case class BlankNullSequence(override val head: Option[SymbolExpression]) extends GenericBlank {
  override def pure = true
  override val expression = symbols.BlankNullSequence(head.toSeq: _*)
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    val range = head match {
      case None => (0 to a.remainingExpressions.size).iterator
      case Some(h) => (0 to a.remainingExpressions.size).iterator.takeWhile(i => i == 0 || a.remainingExpressions(i - 1).head == h)
    }
    for (i <- range) yield PartialBinding(a.binding, a.remainingExpressions.drop(i), a.remainingExpressions.take(i))
  }
  override val length = ZeroOrMore
}
