package org.omath.core.kernel

import org.omath.SymbolExpression
import org.omath.kernel.Kernel

object DownValues {
	def apply(s: SymbolExpression)(implicit kernel: Kernel) = {
	  kernel.kernelState.downValues(s).table.map(_.asExpression)
	}
}