package org.omath.core.arithmetic

import org.omath.symbols
import org.omath.Expression
import org.omath.IntExpression
import org.omath.IntegerExpression

object Times {
  def apply(factors: Seq[Expression]): Expression = {
    val (numeric, others) = factors.partition({ case _: IntegerExpression => true; case _ => false })

    val numericProduct = IntegerExpressionArithmetic.addOption(numeric.map(_.asInstanceOf[IntegerExpression])).toSeq

    // find all the powers, and combine them
    val collected = (others collect {
      case symbols.Power(x, k) => (x, k)
      case x => (x, IntegerExpression(1))
    }).groupBy(_._1).map({
      case (base, Seq((_, IntExpression(1)))) => base
      case (base, exponents) => symbols.Power(base, Plus(exponents.map(_._2)))
    }).toSeq

    symbols.Times((numericProduct ++ collected): _*)
  }
}

