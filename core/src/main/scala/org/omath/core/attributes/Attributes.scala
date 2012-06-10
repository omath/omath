package org.omath.core.attributes

import org.omath.SymbolExpression
import org.omath.kernel.Kernel

object Attributes {
	def apply(symbol: SymbolExpression)(implicit kernel: Kernel): Seq[SymbolExpression] = {
	    kernel.kernelState.attributes(symbol)
	}
}