package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation
import org.omath.util.Scala29Compatibility.+:

private case class FullFormExpressionPattern(headPattern: Pattern, argumentPattern: Pattern) extends Pattern {
  override def asExpression = (headPattern.asExpression)((argumentPattern.asExpression match {
    case FullFormExpression(symbols.Sequence, arguments) => arguments
    case argument => Seq(argument)
  }):_*)
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
  override def names = headPattern.names ++ argumentPattern.names
}
