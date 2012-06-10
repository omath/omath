package org.omath.core.flowcontrol

import org.omath.Expression
import org.omath.kernel.Evaluation

object CompoundExpression {
	def apply(expressions: Expression*)(implicit evaluation: Evaluation): Expression = {
	  (for(expression <- expressions) yield evaluation.evaluate(expression)).lastOption.getOrElse(org.omath.symbols.Null)
	}
}