package org.omath

import org.omath.patterns.ReplacementRuleTable

trait Evaluation { evaluation =>
  def current: Expression = stack.head
  def stack: List[Expression]
  def evaluate(expression: Expression): Expression
}

trait Kernel { kernel: EvaluationStrategy =>
  def kernelState: KernelState
  def evaluate(expression: Expression): Expression = Evaluation(Nil).evaluate(expression)

  case class Evaluation(stack: List[Expression]) extends _root_.org.omath.Evaluation {
    def evaluate(expression: Expression): Expression = {
      Evaluation(expression :: stack).fixedPoint.current
    }

    private def updateCurrent(expression: Expression): Evaluation = Evaluation(expression :: stack.tail)
    private def updateCurrent(iterator: Iterator[Expression]): Evaluation = {
      if (iterator.hasNext) {
        updateCurrent(iterator.next)
      } else {
        this
      }
    }
    def update(rules: ReplacementRuleTable): Evaluation = {
      updateCurrent(rules(current)(this))
    }
    def update(rulesOption: Option[ReplacementRuleTable]): Evaluation = {
      rulesOption match {
        case Some(r) => update(r)
        case None => this
      }
    }

    private def fixedPoint: Evaluation = {
      var a = this
      var b = kernel.evaluateOneStep(this)
      while (a != b) {
        a = b
        b = kernel.evaluateOneStep(a)
      }
      b
    }
  }
}