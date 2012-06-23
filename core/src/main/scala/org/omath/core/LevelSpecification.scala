package org.omath.core

import org.omath.Expression
import org.omath.FullFormExpression
import org.omath.IntExpression
import org.omath.{ symbols => systemSymbols }
import org.omath.bootstrap.conversions.Converter

sealed trait LevelSpecification {
  def heads: Boolean
  def piecesOf(expression: Expression): Iterator[Expression] = {
    val immediate = immediatePiecesOf(expression)
    next match {
      case None => immediate
      case Some(ls) => expression match {
        case expression: FullFormExpression => {
          immediate ++ (if (heads) {
            expression.head +: expression.arguments
          } else {
            expression.arguments
          }).iterator.flatMap(ls.piecesOf(_))
        }
        case _ => immediate
      }
    }
  }
  protected def immediatePiecesOf(expression: Expression): Iterator[Expression]
  protected def next: Option[LevelSpecification]
  def withHeads(heads: Boolean): LevelSpecification
}
object LevelSpecification {
  Converter.registerConversionToInstance({
    case (systemSymbols.List(IntExpression(n)), "org.omath.core.LevelSpecification") if n > 0 => PositiveLevelSpecification(n)
    case (IntExpression(n), "org.omath.core.LevelSpecification") => NonNegativeRangeLevelSpecification(0, n)
    case (systemSymbols.Infinity, "org.omath.core.LevelSpecification") => AllLevelSpecification()
  })

}

case class ZeroLevelSpecification(override val heads: Boolean = false) extends LevelSpecification {
  override def immediatePiecesOf(expression: Expression) = Iterator(expression)
  override def next = None
  override def withHeads(heads: Boolean) = copy(heads = heads)
}
case class PositiveLevelSpecification(n: Int, override val heads: Boolean = false) extends LevelSpecification {
  override def immediatePiecesOf(expression: Expression) = Iterator.empty
  override def next = Some(n match {
    case 1 => ZeroLevelSpecification(heads)
    case _ => copy(n = n - 1)
  })
  override def withHeads(heads: Boolean) = copy(heads = heads)
}
case class NonNegativeRangeLevelSpecification(start: Int, end: Int, override val heads: Boolean = false) extends LevelSpecification {
  override def immediatePiecesOf(expression: Expression) = {
    start match {
      case 0 => Iterator(expression)
      case _ => Iterator.empty
    }
  }
  override def next = if (end == 0) {
    None
  } else {
    Some(copy(start = Seq(0, start - 1).max, end = end - 1))
  }
  override def withHeads(heads: Boolean) = copy(heads = heads)
}
case class SemiInfiniteRangeLevelSpecification(start: Int, override val heads: Boolean = false) extends LevelSpecification {
  override def immediatePiecesOf(expression: Expression) = {
    start match {
      case 0 => Iterator(expression)
      case _ => Iterator.empty
    }
  }
  override def next = Some(if (start == 0) {
    AllLevelSpecification(heads)
  } else {
    copy(start = start - 1)
  })
  override def withHeads(heads: Boolean) = copy(heads = heads)
}
case class AllLevelSpecification(override val heads: Boolean = false) extends LevelSpecification {
  override def immediatePiecesOf(expression: Expression) = Iterator(expression)
  override def next = Some(this)
  override def withHeads(heads: Boolean) = copy(heads = heads)
}