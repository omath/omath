package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class ConditionPattern(val inner: ExpressionPattern, val condition: Expression) extends ExpressionPattern {
  override def expression = symbols.Condition(inner.expression, condition)
  override def pure = false
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    for (
      PartialBinding(binding, remaining, last) <- inner.extend(a);
      if evaluation.evaluate(condition.bind(binding)) == symbols.True
    ) yield PartialBinding(binding, remaining, last)
  }
}

