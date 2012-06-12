package org.omath.core.kernel

import org.omath.kernel.Kernel

object KernelReference {
	def apply(implicit kernel: Kernel) = kernel
}