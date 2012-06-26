package org.omath.core.scoping

import org.omath.Expression
import org.omath.SymbolExpression
import org.omath.symbols
import org.omath.kernel.Evaluation

object With {
	def apply(variables: Seq[Expression], body: Expression)(implicit evaluation: Evaluation): Expression = {
	  // TODO rename variables appearing in inner scopes...
	  
	  val binding = (variables collect {
	    case symbols.Set(lhs: SymbolExpression, rhs: Expression) => (lhs, evaluation.evaluate(rhs))
	  }).toMap
	  
	  body.bind(binding)
	}
}