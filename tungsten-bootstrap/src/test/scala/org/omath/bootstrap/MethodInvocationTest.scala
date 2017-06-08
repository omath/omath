package org.omath.bootstrap

import org.omath.IntegerExpression
import org.omath.SymbolExpression
import org.omath.kernel._
import org.omath.patterns.ReplacementRule
import org.omath.patterns.ReplacementRuleTable

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Matchers
import org.scalatest.FlatSpec

@RunWith(classOf[JUnitRunner])
class MethodInvocationTest extends FlatSpec with Matchers with EvaluationMatchers {

  "method invocation" should "work, unbox arguments and boxing return values" in {
    """JavaMethod["java.util.Date", "toString"][JavaNew["java.util.Date", {1234}], {}]""" should evaluateTo("\"" + new java.util.Date(1234).toString + "\"")
  }

}