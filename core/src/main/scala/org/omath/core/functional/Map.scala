package org.omath.core.functional

import org.omath.Expression

object Map {
  def apply(f: Expression, leaves: Seq[Expression]) = leaves.map(f(_))
}