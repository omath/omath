package org.omath.core.replacements

import org.omath.Expression
import org.omath.FullFormExpression
import org.omath.kernel.Evaluation
import org.omath.patterns.ReplacementRuleTable

object ReplaceAll {
  def apply(expression: Expression, rules: Seq[Expression])(implicit evaluation: Evaluation): Expression = {
    import org.omath.symbols.{ Rule, RuleDelayed }
    implicit val attributes = evaluation.kernel.kernelState.attributes _
    val ruleTable = ReplacementRuleTable(rules collect {
      case Rule(lhs, rhs) => lhs :> rhs
      case RuleDelayed(lhs, rhs) => lhs :> rhs
    })

    def impl(e: Expression): Option[Expression] = {
      ruleTable.apply(e).find(_ => true) match {
        case Some(result) => Some(result)
        case None => {
          e match {
            case FullFormExpression(head, arguments) => {
            	impl(head) match {
            	  case Some(headResult) => {
            	    Some(headResult(arguments.map(a => impl(a).getOrElse(a)):_*))
            	  }
            	  case None => {
            	    val replacedArguments = arguments.map(a => impl(a))
            	    if(replacedArguments.forall(_.isEmpty)) {
            	      None
            	    } else {
            	      Some(head(replacedArguments.zip(arguments).map(p => p._1.getOrElse(p._2)):_*))
            	    }
            	  }
            	}
            }
            case _ => None
          }
        }
      }
    }

    impl(expression).getOrElse(expression)

  }

}