package org.omath

trait Evaluation { evaluation =>
  def current: Expression = stack.head
  def stack: List[Expression]
  def evaluate(expression: Expression): Expression
}

trait Kernel extends net.tqft.toolkit.Logging { kernel: EvaluationStrategy =>
  def kernelState: KernelState
  def evaluate(expression: Expression): Expression = Evaluation(Nil).evaluate(expression)

  case class Evaluation(stack: List[Expression]) extends _root_.org.omath.Evaluation {
    def evaluate(expression: Expression): Expression = {
      Evaluation(expression :: stack).fixedPoint.current
    }

    def updateCurrent(expression: Expression): Evaluation = Evaluation(expression :: stack.tail)
    def updateCurrent(iterator: Iterator[Expression]): Evaluation = {
      if (iterator.hasNext) {
        updateCurrent(iterator.next)
      } else {
        this
      }
    }
    
    private def fixedPoint: Evaluation = {
      import net.tqft.toolkit.functions.FixedPoint._
      (kernel.evaluateOneStep _).fixedPoint(this)
    }
  }
}