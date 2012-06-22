package org.omath.core.patterns

import org.omath.Expression
import org.omath.patterns.Pattern
import org.omath.kernel.Evaluation

object MatchQ {
	def apply(expression: Expression, pattern: Expression)(implicit evaluation: Evaluation): Boolean = {
	  implicit val attributes = evaluation.kernel.kernelState.attributes _
	  val _pattern: Pattern = pattern
	  _pattern.matching(expression).nonEmpty
	}
}