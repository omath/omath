package org.omath.kernel.tungsten

import org.omath._
import org.omath.kernel.Evaluation
import org.omath.kernel.Kernel
import org.omath.patterns._
import org.omath.kernel.KernelState
import org.omath.parser.SyntaxParser
import org.omath.util.Result

trait AbstractKernel { abstractKernel: Kernel with EvaluationStrategy =>
  override def newEvaluation: Evaluation = _Evaluation(Nil)

  private[kernel] case class _Evaluation(stack: List[Expression]) extends Evaluation {
    def kernel = abstractKernel

    def evaluate(expression: Expression): Expression = {
      _Evaluation(expression :: stack).fixedPoint.current
    }

    def update(expression: Expression): _Evaluation = _Evaluation(expression :: stack.tail)
    def update(iterator: Iterator[Expression]): _Evaluation = {
      if (iterator.hasNext) {
        update(iterator.next)
      } else {
        this
      }
    }
    def update(expressions: Iterable[Expression]): _Evaluation = update(expressions.iterator)
    def update(rules: ReplacementRuleTable): _Evaluation = {
      update(rules(current)(this))
    }
    def updateUsing(rules: Iterator[ReplacementRuleTable]): _Evaluation = {
      if (rules.hasNext) {
        update(rules.next)
      } else {
        this
      }
    }

    private def fixedPoint: _Evaluation = {
      var a = this
      var b = abstractKernel.evaluateOneStep(this)
      while (a != b) {
        a = b
        b = abstractKernel.evaluateOneStep(a)
      }
      a // better to return a, to avoid unnecessary reduplication
    }
  }

}

trait Tungsten extends Kernel with AbstractKernel with CompositeEvaluationStrategy { kernel: SyntaxParser =>
  // mention PatternBuilder, so it registers itself with the Pattern companion object
  PatternBuilder
}