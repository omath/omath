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
class EvaluationTest extends FlatSpec with Matchers with EvaluationMatchers {

   "evaluateOneStep" should "not apply a DownValue immediately after an OwnValue" in {
    """f := g[k]
       g[k] := 0
       k := h
       g[h] := 1
       f""" should evaluateTo ("1")
  }

}