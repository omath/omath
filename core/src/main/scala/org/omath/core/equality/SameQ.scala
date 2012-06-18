package org.omath.core.equality

import org.omath.Expression
import org.omath.util.Scala29Compatibility.+:

object SameQ {
  def apply(values: Expression*): Boolean = {
    values match {
      case Seq() => true
      case head +: tail => tail.forall(_ == head)
    }
  }
}