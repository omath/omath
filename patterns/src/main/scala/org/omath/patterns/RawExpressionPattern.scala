package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class RawExpressionPattern(override val expression: RawExpression) extends ExpressionPattern {
  override def pure = true
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    a.remainingExpressions match {
      case (h: RawExpression) +: t if h == expression => Iterator(PartialBinding(a.binding, t, Seq(h)))
      case _ => Iterator.empty
    }
  }
}
