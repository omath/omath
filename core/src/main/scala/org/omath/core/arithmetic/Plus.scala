package org.omath.core.arithmetic

import org.omath.Expression
import org.omath.IntegerExpression
import org.omath.FullFormExpression
import org.omath.symbols
import net.tqft.toolkit.Logging

object Plus extends Logging {
  def apply(terms: Seq[Expression]): Expression = {
    val (numeric, others) = terms.partition({ case _: IntegerExpression => true; case _ => false })

    val numericSum = IntegerExpressionArithmetic.addOption(numeric.map(_.asInstanceOf[IntegerExpression])).toSeq

    def oneIdentityTimes(factors: Seq[Expression]) = {
      require(factors.nonEmpty)
      if(factors.size == 1) {
        factors.head
      } else {
        symbols.Times(factors:_*)
      }
    }
    
    // collect all the other terms
    val split = others.map({
      case FullFormExpression(symbols.Times, termFactors) => termFactors.partition(_.isInstanceOf[IntegerExpression])
      case other => (Seq.empty, Seq(other))
    }).asInstanceOf[Seq[(Seq[IntegerExpression], Seq[Expression])]].map(p => (p._1.ensuring(_.size <= 1).headOption, p._2))
    
    val collected = split.groupBy(_._2).mapValues(s => IntegerExpressionArithmetic.addOption(s.flatMap(_._1)).toSeq)

    val result = symbols.Plus(numericSum ++ collected.toSeq.map(p => oneIdentityTimes(p._2 ++ p._1)):_*)
    
    info("Plus returned: " + result)
    result
    
  }
}