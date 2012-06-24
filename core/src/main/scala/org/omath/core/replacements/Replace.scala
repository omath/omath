package org.omath.core.replacements

import org.omath.Expression
import org.omath.FullFormExpression
import org.omath.kernel.Evaluation
import org.omath.patterns.ReplacementRuleTable
import org.omath.core.LevelSpecification

object Replace {
  def apply(expression: Expression, rules: ReplacementRuleTable, levelSpecification: LevelSpecification)(implicit evaluation: Evaluation): Expression = {
    levelSpecification.replacing(rules)(expression)
  }
}