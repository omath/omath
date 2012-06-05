package org.omath.kernel

import org.omath._
import net.tqft.toolkit.Logging

trait TrivialEvaluationStrategy extends EvaluationStrategy { es: Kernel =>
  override def evaluateOneStep(evaluation: Evaluation) = evaluation
}

trait OwnValueEvaluation extends EvaluationStrategy { es: Kernel =>
  def findOwnValues(expression: Expression) = {
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
  def findDownValues(expression: Expression) = {
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

trait CompositeEvaluationStrategy
  extends TrivialEvaluationStrategy
  with OwnValueEvaluation
  with DownValueEvaluation { es: Kernel => }
