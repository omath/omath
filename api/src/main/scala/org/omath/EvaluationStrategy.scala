package org.omath

trait EvaluationStrategy { es: Kernel =>
  def evaluateOneStep(evaluation: Evaluation): Evaluation
}
