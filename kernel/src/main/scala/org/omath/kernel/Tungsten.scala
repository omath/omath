package org.omath.kernel

import org.omath.Kernel
import org.omath.patterns.PatternBuilder

trait Tungsten extends Kernel with CompositeEvaluationStrategy {
  // mention PatternBuilder, so it registers itself with the Pattern companion object
  PatternBuilder
}