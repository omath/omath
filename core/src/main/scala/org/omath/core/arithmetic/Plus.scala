package org.omath.core.arithmetic

import org.omath.Expression
import org.omath.IntegerExpression

object Plus {
  def apply(terms: Seq[Expression]): Expression = {
    val (numeric, others) = terms.partition({ case _: IntegerExpression => true; case _ => false })

    val numericSum = numeric.map(_.asInstanceOf[IntegerExpression].toApint).reduceOption(_.add(_)).map(IntegerExpression(_)).toSeq

    // collect all the other terms
    
    import org.omath.util.Scala29Compatibility.???
    ???
  }
}