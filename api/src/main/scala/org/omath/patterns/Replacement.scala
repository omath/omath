package org.omath.patterns

import org.omath.Evaluation
import org.omath.Expression

trait Replacement {
  def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression]
}
case class ReplacementRuleTable(table: ReplacementRule*) extends Replacement {
  override def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression] = {
    for (rule <- table.iterator; r <- rule(x)) yield r
  }
}
object ReplacementRuleTable {
  implicit def singletonTable(rule: ReplacementRule) = ReplacementRuleTable(rule)
}

case class ReplacementRule(pattern: Pattern, result: Expression) extends Replacement {
  override def apply(x: Expression)(implicit evaluation: Evaluation): Iterator[Expression] = {
    for (b <- pattern.bind(x)) yield result.bind(b)
  }
}
