package org.omath.core

import org.omath.Expression
import org.omath.FullFormExpression
import org.omath.IntExpression
import org.omath.{ symbols => systemSymbols }
import org.omath.bootstrap.conversions.Converter
import org.omath.patterns.ReplacementRuleTable
import org.omath.kernel.Evaluation

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
  def replacing(rules: ReplacementRuleTable)(expression: Expression)(implicit evaluation: Evaluation): Expression = {
    (if (active) {
      rules.applyOption(expression)
    } else {
      None
    }) match {
      case Some(result) => result
      case None => {
        // burrow deeper
        expression match {
          case expression: FullFormExpression => {
            next match {
              case None => expression
              case Some(ls) => {
                (if (heads) {
                  ls.replacing(rules)(expression.head)
                } else {
                  expression.head
                })(expression.arguments.map(a => ls.replacing(rules)(a)): _*)
              }
            }
          }
          case _ => expression
        }
      }
    }
  }
  protected def active: Boolean
  private def immediatePiecesOf(expression: Expression) = if (active) {
    Iterator(expression)
  } else {
    Iterator.empty
  }
  protected def next: Option[LevelSpecification]
  def withHeads(heads: Boolean): LevelSpecification
}
object LevelSpecification {
  Converter.registerConversionToInstance({
    case (systemSymbols.List(IntExpression(0)), "org.omath.core.LevelSpecification") => ZeroLevelSpecification()
    case (systemSymbols.List(IntExpression(n)), "org.omath.core.LevelSpecification") if n > 0 => PositiveLevelSpecification(n)
    case (IntExpression(n), "org.omath.core.LevelSpecification") => NonNegativeRangeLevelSpecification(0, n)
    case (systemSymbols.Infinity, "org.omath.core.LevelSpecification") => AllLevelSpecification()
  })

}

case class ZeroLevelSpecification(override val heads: Boolean = false) extends LevelSpecification {
  override val active = true
  override val next = None
  override def withHeads(heads: Boolean) = copy(heads = heads)
}
case class PositiveLevelSpecification(n: Int, override val heads: Boolean = false) extends LevelSpecification {
  override val active = false
  override def next = Some(n match {
    case 1 => ZeroLevelSpecification(heads)
    case _ => copy(n = n - 1)
  })
  override def withHeads(heads: Boolean) = copy(heads = heads)
}
case class NonNegativeRangeLevelSpecification(start: Int, end: Int, override val heads: Boolean = false) extends LevelSpecification {
  override val active = start == 0
  override def next = if (end == 0) {
    None
  } else {
    Some(copy(start = Seq(0, start - 1).max, end = end - 1))
  }
  override def withHeads(heads: Boolean) = copy(heads = heads)
}
case class SemiInfiniteRangeLevelSpecification(start: Int, override val heads: Boolean = false) extends LevelSpecification {
  override val active = start == 0
  override def next = Some(if (start == 0) {
    AllLevelSpecification(heads)
  } else {
    copy(start = start - 1)
  })
  override def withHeads(heads: Boolean) = copy(heads = heads)
}
case class AllLevelSpecification(override val heads: Boolean = false) extends LevelSpecification {
  override val active = true
  override def next = Some(this)
  override def withHeads(heads: Boolean) = copy(heads = heads)
}