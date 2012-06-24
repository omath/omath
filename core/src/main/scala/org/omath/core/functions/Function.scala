package org.omath.core.functions

import org.omath.symbols
import org.omath.Expression
import org.omath.SymbolExpression
import org.omath.patterns.ReplacementRuleTable
import org.omath.kernel.Evaluation
import org.omath.core.replacements.ReplaceAll

object Function {
  val k = SymbolExpression("k")
  def slotRules(arguments: Seq[Expression]) = {
    // no attributes need, we're just defining local rules for Slot and SlotSequence
    implicit val attributes = { s: SymbolExpression => Seq.empty }
    ReplacementRuleTable((for((a, i) <- arguments.zipWithIndex) yield {
      symbols.Slot(i + 1) :> a
    }))
  }
	def apply(variables: Seq[SymbolExpression], body: Expression, arguments: Seq[Expression])(implicit evaluation: Evaluation): Expression = {
	  val binding = variables.zip(arguments).toMap
	  val boundBody = body.bind(binding)
	  ReplaceAll(boundBody, slotRules(arguments))
	}
}