package org.omath.kernel

import org.omath._
import net.tqft.toolkit.Logging
import org.omath.patterns.ReplacementRuleTable

trait TrivialEvaluationStrategy extends EvaluationStrategy { es: Kernel =>
  override def evaluateOneStep(evaluation: Evaluation) = evaluation
}

trait OwnValueEvaluation extends EvaluationStrategy { es: Kernel =>
  private def findOwnValues(expression: Expression) = {
    expression match {
      case symbol: SymbolExpression => Some({
        val ownValues = kernelState.ownValues(symbol)
        Logging.info("Found OwnValues for " + symbol + ": " + ownValues)
        ownValues
      })
      case _ => {
        None
      }
    }
  }

  abstract override def evaluateOneStep(evaluation: Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(findOwnValues(previousStep.current))
  }
}

trait DownValueEvaluation extends EvaluationStrategy { es: Kernel =>
  private def findDownValues(expression: Expression) = {
    expression match {
      case FullFormExpression(symbolicHead: SymbolExpression, arguments) => Some(kernelState.downValues(symbolicHead))
      case _ => None
    }
  }

  abstract override def evaluateOneStep(evaluation: Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(findDownValues(previousStep.current))
  }
}

trait SubValueEvaluation extends EvaluationStrategy { es: Kernel =>
  @scala.annotation.tailrec
  private def findSubValues(expression: Expression): Option[ReplacementRuleTable] = {
    expression match {
      case FullFormExpression(symbolicHead: SymbolExpression, _) => Some(kernelState.subValues(symbolicHead))
      case FullFormExpression(other, _) => findSubValues(other)
      case _ => None
    }
  }

  abstract override def evaluateOneStep(evaluation: Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(findSubValues(previousStep.current))
  }
}

trait UpValueEvaluation extends EvaluationStrategy { es: Kernel =>
  private def findUpValues(expression: Expression): Seq[ReplacementRuleTable] = {
    expression match {
      case FullFormExpression(_, arguments) => arguments.collect({
        case s: SymbolExpression => s	// TODO is this right? perhaps leave this out?
        case FullFormExpression(s: SymbolExpression, _) => s
      }).map(kernelState.upValues(_))
      case _ => Nil
    }
  }

  abstract override def evaluateOneStep(evaluation: Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(findUpValues(previousStep.current))
  }
}

trait CompositeEvaluationStrategy
  extends TrivialEvaluationStrategy
  with OwnValueEvaluation
  with DownValueEvaluation
  with SubValueEvaluation
  with UpValueEvaluation  { es: Kernel => }
