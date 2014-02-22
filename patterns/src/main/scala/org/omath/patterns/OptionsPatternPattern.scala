package org.omath.patterns

import org.omath.symbols
import org.omath.kernel.Evaluation

object OptionsPatternPattern extends Pattern {
  override val asExpression = symbols.OptionsPattern()
  override def pure = true
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    ???
  }
  override def names = Seq.empty
}
