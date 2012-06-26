package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class ConditionPattern(val inner: Pattern, val condition: Expression) extends Pattern {
  override def asExpression = symbols.Condition(inner.asExpression, condition)
  override def pure = false
  override def names = inner.names
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    for (
      PartialBinding(binding, remaining, last) <- inner.extend(a);
      if evaluation.evaluate(condition.bind(binding)) == symbols.True
    ) yield PartialBinding(binding, remaining, last)
  }
}

