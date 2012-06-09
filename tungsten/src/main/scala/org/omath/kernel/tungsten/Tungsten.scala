package org.omath.kernel.tungsten

import org.omath._
import org.omath.kernel.Evaluation;
import org.omath.kernel.Kernel;
import org.omath.patterns._

trait AbstractKernel extends Kernel { kernel: EvaluationStrategy =>
  override def evaluate(expression: Expression): Expression = _Evaluation(Nil).evaluate(expression)

  private[kernel] case class _Evaluation(stack: List[Expression]) extends Evaluation {
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
    @Deprecated
    def updateUsing(rules: Traversable[ReplacementRuleTable]): _Evaluation = {
      rules.headOption.map(update).getOrElse(this)
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
      var b = kernel.evaluateOneStep(this)
      while (a != b) {
        a = b
        b = kernel.evaluateOneStep(a)
      }
      a // better to return a, to avoid unnecessary reduplication
    }
  }

}

trait Tungsten extends AbstractKernel with CompositeEvaluationStrategy {
  // mention PatternBuilder, so it registers itself with the Pattern companion object
  PatternBuilder
}