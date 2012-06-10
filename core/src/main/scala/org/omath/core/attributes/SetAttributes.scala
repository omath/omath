package org.omath.core.attributes

import org.omath.SymbolExpression
import org.omath.kernel.Kernel

object SetAttributes {
	def apply(symbols: Seq[SymbolExpression], attributes: Seq[SymbolExpression])(implicit kernel: Kernel) {
	  for(symbol <- symbols) {
	    kernel.kernelState.addAttributes(symbol, attributes:_*)
	  }
	}
}