package org.omath.patterns

import org.omath.Expression
import org.omath.kernel.Evaluation

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.util.Scala29Compatibility.???

trait PatternTest extends FlatSpec with ShouldMatchers {
  // mention PatternBuilder, so it registers itself with the Pattern companion object
  PatternBuilder

  implicit object MockEvaluation extends Evaluation {
    override def stack = Nil
    override def evaluate(expression: Expression) = ???
    override def kernel = ???
  }  
}
