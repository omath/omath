package org.omath.patterns

import org.omath.Bindable
import org.omath.Expression
import org.omath.symbols
import org.omath.kernel.Evaluation

trait Replacement {
  def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression]
  def applyOption(x: Expression)(implicit evaluation: Evaluation): Option[Expression] = {
    val iterator = apply(x)
    if (iterator.hasNext) {
      Some(iterator.next)
    } else {
      None
    }
  }
  def applyOrSelf(x: Expression)(implicit evaluation: Evaluation): Expression = {
    applyOption(x).getOrElse(x)
  }
  def asExpression: Expression
}

case class ReplacementRule(pattern: Pattern, result: Bindable) extends Replacement {
  override def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression] = {
    for (b <- pattern.matching(x)) yield result.activeBind(b)
  }
  override def asExpression = symbols.RuleDelayed(
    pattern match {
      case pattern: ExpressionPattern => symbols.HoldPattern(pattern.expression)
    },
    result match {
      case expression: Expression => expression
    })
}

case class ReplacementRuleTable(table: Seq[ReplacementRule]) extends Replacement {
  override def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression] = {
    for (rule <- table.iterator; r <- rule(x)) yield r
  }
  override def asExpression = symbols.List(table.map(_.asExpression): _*)

  def +(rule: ReplacementRule): ReplacementRuleTable = {
    val index = table.indexWhere({ other =>
      Pattern.tryCompare(rule.pattern, other.pattern) match {
        case Some(n) if n <= 0 => true
        case _ => false
      }
    })
    ReplacementRuleTable(index match {
      case -1 => table :+ rule
      case k => {
        Pattern.tryCompare(rule.pattern, table(k).pattern) match {
          case Some(0) => table.take(k) ++ (rule +: table.drop(k + 1))
          case Some(n) if n < 0 => table.take(k) ++ (rule +: table.drop(0))
          case _ => org.omath.util.Scala29Compatibility.???
        }
      }
    })
  }

  def ++(rules: Seq[ReplacementRule]): ReplacementRuleTable = {
    // FIXME this is stuff that really doesn't belong in the api package...
    // ReplacementRuleTable(table.filterNot({ a: ReplacementRule => rules.exists({ b: ReplacementRule => a.pattern == b.pattern }) }) ++ rules)
    rules.foldLeft(this)(_ + _)    
  }
}

object ReplacementRuleTable {
  import org.omath.util.Scala29Compatibility._
  import language.implicitConversions
  implicit def singletonTable(rule: ReplacementRule) = ReplacementRuleTable(Seq(rule))
  val empty = ReplacementRuleTable(Seq.empty)
}
