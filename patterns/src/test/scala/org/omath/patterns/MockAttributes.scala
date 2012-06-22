package org.omath.patterns

import org.omath.SymbolExpression

trait MockAttributes {
  implicit val attributes = { s: SymbolExpression => Seq.empty }
}