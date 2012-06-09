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
class EvaluationTest extends FlatSpec with ShouldMatchers {

  import org.omath.symbols.{ SetDelayed, Null, List, Pattern, Blank }

  "evaluateOneStep" should "not apply a DownValue immediately after an OwnValue" in {
    ///  FIXME
    TungstenBootstrap.evaluateSyntax("""
    		f := g[k]
    		g[k] := 0
    		k := h
    		g[h] := 1
    		f
        """).get should equal(IntegerExpression(1))
  }

}