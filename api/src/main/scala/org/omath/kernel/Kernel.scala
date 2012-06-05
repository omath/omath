package org.omath.kernel

import org.omath.Expression
import org.omath.patterns.ReplacementRuleTable

trait Evaluation { evaluation =>
  def current: Expression = stack.head
  def stack: List[Expression]
  def evaluate(expression: Expression): Expression
}

trait Kernel {
  def kernelState: KernelState
  def evaluate(expression: Expression): Expression
}