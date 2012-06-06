package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class FullFormExpressionPattern(override val expression: FullFormExpression, headPattern: Pattern, argumentPattern: Pattern) extends ExpressionPattern {
  override lazy val pure = headPattern.pure && argumentPattern.pure
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    a match {
      case PartialBinding(b1, (h: FullFormExpression) +: t, _) => {
        for (b2 <- headPattern.extend(b1)(h.head); b3 <- argumentPattern.extend(b2)(h.arguments: _*)) yield {
          PartialBinding(b3, t, Seq(h))
        }
      }
      case _ => Iterator.empty
    }
  }
}
