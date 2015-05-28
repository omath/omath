package org.omath.patterns

import org.omath.Expression
import org.omath.kernel.Evaluation

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._

trait PatternTest extends FlatSpec with Matchers {
  // mention PatternBuilder, so it registers itself with the Pattern companion object
  PatternBuilder

  implicit object MockEvaluation extends Evaluation {
    override def stack = Nil
    override def evaluate(expression: Expression) = ???
    override def kernel = ???
  }  
}
