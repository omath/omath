package org.omath.core.eval

import net.tqft.toolkit.eval.Eval

object ScalaEval {
	def apply(code: String): Any = Eval(code).get
}