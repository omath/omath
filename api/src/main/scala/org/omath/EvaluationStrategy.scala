package org.omath

trait EvaluationStrategy { es: Kernel =>
  def evaluateOneStep(evaluation: Evaluation): Evaluation
}

trait TrivialEvaluationStrategy extends EvaluationStrategy { es: Kernel =>
  override def evaluateOneStep(evaluation: Evaluation) = evaluation
}

trait OwnValueEvaluation extends EvaluationStrategy { es: Kernel =>
  def findOwnValues(expression: Expression) = {
    expression match {
      case symbol: SymbolExpression => Some({
        val ownValues = kernelState.ownValues(symbol)
        info("Found OwnValues for " + symbol + ": " + ownValues)
        ownValues
      })
      case _ => {
        None
      }
    }
  }

  abstract override def evaluateOneStep(evaluation: Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    findOwnValues(previousStep.current) match {
      case Some(rules) => {
        previousStep.updateCurrent(rules(previousStep.current)(previousStep))
      }
      case None => previousStep
    }
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
    findDownValues(previousStep.current) match {
      case Some(rules) => {
        previousStep.updateCurrent(rules(previousStep.current)(previousStep))
      }
      case None => previousStep
    }
  }

}

trait CompositeEvaluationStrategy
  extends TrivialEvaluationStrategy
  with OwnValueEvaluation
  with DownValueEvaluation { es: Kernel => }
