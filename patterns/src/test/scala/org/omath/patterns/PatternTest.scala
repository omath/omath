package org.omath.patterns

import org.omath.Expression
import org.omath.kernel.Evaluation

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

trait PatternTest extends FlatSpec with ShouldMatchers {
  // mention PatternBuilder, so it registers itself with the Pattern companion object
  PatternBuilder

  implicit object MockEvaluation extends Evaluation {
    override def stack = Nil
    override def evaluate(expression: Expression) = ???
    override def kernel = ???
  }  
}
