package org.omath.patterns

import org.omath.symbols
import org.omath.kernel.Evaluation

object OptionsPatternPattern extends ExpressionPattern {
  override def expression = symbols.OptionsPattern()
  override def pure = true
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    import org.omath.util.Scala29Compatibility.???
    ???
  }
}
