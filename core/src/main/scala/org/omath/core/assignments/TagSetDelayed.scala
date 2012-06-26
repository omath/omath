package org.omath.core.assignments

import org.omath.symbols
import org.omath.SymbolExpression
import org.omath.Expression
import org.omath.FullFormExpression
import org.omath.kernel.Kernel
import org.omath.patterns.Pattern
import org.omath.kernel.Evaluation

object TagSetDelayed {
  def apply(s: SymbolExpression, lhs: Pattern, rhs: Expression)(implicit evaluation: Evaluation): Expression = {
    implicit val attributes = evaluation.kernel.kernelState.attributes _

    val evaluatedLHS: Pattern = lhs.evaluateArguments
    val unwrappedLHS = evaluatedLHS.unwrap

    // make sure that s is findable in the lhs
    unwrappedLHS match {
      case FullFormExpression(head, arguments) if (arguments.exists(_.head == s)) => {
        evaluation.kernel.kernelState.addUpValues(s, evaluatedLHS :> rhs)
      }
      case _ => {
        System.err.println("The symbol " + s + " isn't the head of an argument in " + evaluatedLHS + ", so it's impossible to attach an UpValue to it.")
      }
    }

    symbols.Null
  }
}