package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation
import org.omath.util.Scala29Compatibility.+:

private case class RawExpressionPattern(override val asExpression: RawExpression) extends Pattern {
  override def pure = true
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    a.remainingExpressions match {
      case (h: RawExpression) +: t if h == asExpression => Iterator(PartialBinding(a.binding, t, Seq(h)))
      case _ => Iterator.empty
    }
  }
  override def names = Seq.empty
}
