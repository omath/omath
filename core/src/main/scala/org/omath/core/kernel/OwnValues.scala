package org.omath.core.kernel

import org.omath.SymbolExpression
import org.omath.kernel.Kernel

object OwnValues {
	def apply(s: SymbolExpression)(implicit kernel: Kernel) = {
	  kernel.kernelState.ownValues(s)
	}
}