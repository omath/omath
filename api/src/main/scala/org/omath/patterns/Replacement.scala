package org.omath.patterns

import org.omath.Bindable
import org.omath.Expression
import org.omath.symbols
import org.omath.kernel.Evaluation

trait Replacement {
  def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression]
  def asExpression: Expression
}
case class ReplacementRuleTable(table: ReplacementRule*) extends Replacement {
  override def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression] = {
    for (rule <- table.iterator; r <- rule(x)) yield r
  }
  override def asExpression = symbols.List(table.map(_.asExpression):_*)
}
object ReplacementRuleTable {
  import language.implicitConversions
  implicit def singletonTable(rule: ReplacementRule) = ReplacementRuleTable(rule)
}

case class ReplacementRule(pattern: Pattern, result: Bindable) extends Replacement {
  override def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression] = {
    for (b <- pattern.bind(x)) yield result.bind(b)
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
