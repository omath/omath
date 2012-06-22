package org.omath.core.strings

import org.omath.Expression
import org.omath.kernel.Evaluation

object ToString {
  def apply(expression: Expression)(implicit evaluation: Evaluation) = expression.toContextualString
}