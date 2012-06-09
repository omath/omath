package org.omath.bootstrap

import org.omath.IntegerExpression
import org.omath.SymbolExpression
import org.omath.kernel._
import org.omath.patterns.ReplacementRule
import org.omath.patterns.ReplacementRuleTable

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

@RunWith(classOf[JUnitRunner])
class MethodInvocationTest extends FlatSpec with ShouldMatchers with EvaluationMatchers {

  "method invocation" should "work, and unbox arguments" in {
    """JavaMethod["java.util.Date", "toString"][JavaNew["java.util.Date", {1234}], {}]""" should evaluateTo("\"" + new java.util.Date(1234).toString + "\"")
  }

}