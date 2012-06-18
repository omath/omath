package org.omath.patterns

import org.omath.Bindable
import org.omath.Expression
import org.omath.symbols
import org.omath.kernel.Evaluation

trait Replacement {
  def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression]
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
		  }
  )
}

case class ReplacementRuleTable(table: Seq[ReplacementRule]) extends Replacement {
  override def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression] = {
    for (rule <- table.iterator; r <- rule(x)) yield r
  }
  override def asExpression = symbols.List(table.map(_.asExpression):_*)
  
  def ++(rules: Seq[ReplacementRule]): ReplacementRuleTable = {
    // FIXME this is stuff that really doesn't belong in the api package...
    // TODO rule specificity, etc.
    ReplacementRuleTable(table.filterNot({a: ReplacementRule => rules.exists({  b: ReplacementRule => a.pattern == b.pattern})}) ++ rules)
  }
}

object ReplacementRuleTable {
  import org.omath.util.Scala29Compatibility._
  import language.implicitConversions
  implicit def singletonTable(rule: ReplacementRule) = ReplacementRuleTable(Seq(rule))
  val empty = ReplacementRuleTable(Seq.empty)
}
