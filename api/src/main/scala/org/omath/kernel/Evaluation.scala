package org.omath.kernel

import org.omath._

trait Evaluation { evaluation =>
  def current: Expression = stack.head
  def stack: List[Expression]
  def evaluate(expression: Expression): Expression
  
  def kernel: Kernel 
}
