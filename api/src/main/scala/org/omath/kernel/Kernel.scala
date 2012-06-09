package org.omath.kernel

import org.omath.Expression
import org.omath.patterns.ReplacementRuleTable
import org.omath.SymbolExpression

trait Evaluation { evaluation =>
  def current: Expression = stack.head
  def stack: List[Expression]
  def evaluate(expression: Expression): Expression
}

trait Kernel {
  def kernelState: KernelState
  def evaluate(expression: Expression): Expression

  // this will be overriden later, but is needing for unit testing
  protected def symbolizer = { s: String => SymbolExpression(s, "System") }
}