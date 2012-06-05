package org.omath.kernel

import org.omath._
import org.omath.patterns.ReplacementRuleTable

import net.tqft.toolkit.Logging

trait EvaluationStrategy { es: AbstractKernel =>
  def evaluateOneStep(evaluation: _Evaluation): _Evaluation
}

trait TrivialEvaluationStrategy extends EvaluationStrategy { es: AbstractKernel =>
  override def evaluateOneStep(evaluation: _Evaluation) = evaluation
}

trait HeadEvaluation extends EvaluationStrategy { es: AbstractKernel =>
  abstract override def evaluateOneStep(evaluation: _Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(
      previousStep.current match {
        case FullFormExpression(head, arguments) => Some(FullFormExpression(evaluation.evaluate(head), arguments))
        case _ => None
      })
  }
}

trait SequenceFlattening extends EvaluationStrategy { es: AbstractKernel =>
  abstract override def evaluateOneStep(evaluation: _Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(
      previousStep.current match {
        case FullFormExpression(head, arguments) => {
          // TODO respect SequenceHold
          // TODO if nothing changes, return the original Expression
          Some(FullFormExpression(head, arguments.flatMap({
            case FullFormExpression(symbols.Sequence, a) => a
            case b => Seq(b)
          })))
        }
        case _ => None
      })
  }
}

trait ArgumentEvaluation extends EvaluationStrategy { es: AbstractKernel =>
  abstract override def evaluateOneStep(evaluation: _Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(
      previousStep.current match {
        case FullFormExpression(head, arguments) => {
          // TODO decide which arguments are meant to be held
          // TODO is nothing changes, return the original Expression
          Some(FullFormExpression(head, arguments.map(evaluation.evaluate(_))))
        }
        case _ => None
      })
  }
}

trait OwnValueEvaluation extends EvaluationStrategy { es: AbstractKernel =>
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

  abstract override def evaluateOneStep(evaluation: _Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(findOwnValues(previousStep.current))
  }
}

trait DownValueEvaluation extends EvaluationStrategy { es: AbstractKernel =>
  private def findDownValues(expression: Expression) = {
    expression match {
      case FullFormExpression(symbolicHead: SymbolExpression, arguments) => Some(kernelState.downValues(symbolicHead))
      case _ => None
    }
  }

  abstract override def evaluateOneStep(evaluation: _Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(findDownValues(previousStep.current))
  }
}

trait SubValueEvaluation extends EvaluationStrategy { es: AbstractKernel =>
  @scala.annotation.tailrec
  private def findSubValues(expression: Expression): Option[ReplacementRuleTable] = {
    expression match {
      case FullFormExpression(symbolicHead: SymbolExpression, _) => Some(kernelState.subValues(symbolicHead))
      case FullFormExpression(other, _) => findSubValues(other)
      case _ => None
    }
  }

  abstract override def evaluateOneStep(evaluation: _Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(findSubValues(previousStep.current))
  }
}

trait UpValueEvaluation extends EvaluationStrategy { es: AbstractKernel =>
  private def findUpValues(expression: Expression): Seq[ReplacementRuleTable] = {
    expression match {
      case FullFormExpression(_, arguments) => arguments.collect({
        case s: SymbolExpression => s // TODO is this right? perhaps leave this out?
        case FullFormExpression(s: SymbolExpression, _) => s
      }).map(kernelState.upValues(_))
      case _ => Nil
    }
  }

  abstract override def evaluateOneStep(evaluation: _Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    previousStep.update(findUpValues(previousStep.current))
  }
}

trait CompositeEvaluationStrategy
  extends TrivialEvaluationStrategy
  with HeadEvaluation
  with SequenceFlattening
  with ArgumentEvaluation
  with OwnValueEvaluation
  with DownValueEvaluation
  with SubValueEvaluation
  with UpValueEvaluation { es: AbstractKernel => }
