package org.omath.core.kernel

import org.omath.SymbolExpression
import org.omath.kernel.Kernel

object UpValues {
	def apply(s: SymbolExpression)(implicit kernel: Kernel) = {
	  kernel.kernelState.upValues(s).table.map(_.asExpression)
	}
}