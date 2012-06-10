package org.omath.kernel.tungsten

import org.omath._
import org.omath.kernel._
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

// FIXME I think this is meant to be achieved by an UpValue on Sequence!
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
          val attributes: Seq[SymbolExpression] = head match {
            case head: SymbolExpression => kernelState.attributes(head)
            case _ => Seq.empty
          }
          val holdFirst = attributes.contains(symbols.HoldFirst) || attributes.contains(symbols.HoldAll)
          val holdRest = attributes.contains(symbols.HoldRest) || attributes.contains(symbols.HoldAll)
          // TODO evaluate Evaluate (and maybe JavaClass, etc?) anyway // NB maybe that's meant to be achieved via UpValues??
          // TODO if nothing changes, return the original Expression
          Some(FullFormExpression(head, arguments match {
            case Nil => arguments
            case h +: t => {
              (if (holdFirst) h else evaluation.evaluate(h)) +: (t.map({ x => if (holdRest) x else evaluation.evaluate(x) }))
            }
          }))
        }
        case _ => None
      })
  }
}

trait ValueEvaluation extends EvaluationStrategy { es: AbstractKernel =>
  private def findOwnValues(expression: Expression): Option[ReplacementRuleTable] = {
    expression match {
      case symbol: SymbolExpression => Some({
        val ownValues = kernelState.ownValues(symbol)
        if (ownValues.table.nonEmpty) Logging.info("Found OwnValues for " + symbol + ": " + ownValues)
        ownValues
      })
      case _ => {
        None
      }
    }
  }
  private def findUpValues(expression: Expression): Seq[ReplacementRuleTable] = {
    expression match {
      case FullFormExpression(_, arguments) => arguments.collect({
        case s: SymbolExpression => s // TODO is this right? perhaps leave this out?
        case FullFormExpression(s: SymbolExpression, _) => s
      }).map(kernelState.upValues(_))
      case _ => Nil
    }
  }
  private def findDownValues(expression: Expression): Option[ReplacementRuleTable] = {
    expression match {
      case FullFormExpression(symbolicHead: SymbolExpression, arguments) => Some(kernelState.downValues(symbolicHead))
      case _ => None
    }
  }
  private def findSubValues(expression: Expression): Option[ReplacementRuleTable] = {
    if (expression.headDepth > 1) {
      val symbol = expression.symbolHead
      val subValues = kernelState.subValues(symbol)
      if (subValues.table.nonEmpty) Logging.info("Found SubValues for " + symbol + ": " + subValues)
      Some(subValues)
    } else {
      None
    }
  }

  private def constructIterator[A](g: (() => Traversable[A])*) = {
    g.iterator.map(_()).flatten
  }
  
  abstract override def evaluateOneStep(evaluation: _Evaluation) = {
    val previousStep = super.evaluateOneStep(evaluation)
    val c = previousStep.current
    // TODO is this the right order?
    previousStep.updateUsing(constructIterator(
        () => findOwnValues(c),
        () => findDownValues(c),
        () => findSubValues(c),
        () => findUpValues(c)
        ))
  }
}


trait CompositeEvaluationStrategy
  extends TrivialEvaluationStrategy
  with HeadEvaluation
  with SequenceFlattening
  with ArgumentEvaluation
  with ValueEvaluation
  { es: AbstractKernel => }
