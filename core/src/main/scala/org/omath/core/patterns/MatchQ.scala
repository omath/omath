package org.omath.core.patterns

import org.omath.Expression
import org.omath.patterns.Pattern
import org.omath.kernel.Evaluation

object MatchQ {
	def apply(expression: Expression, pattern: Pattern)(implicit evaluation: Evaluation): Boolean = {
	  pattern.matching(expression).nonEmpty
	}
}