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
class SetDelayedTest extends FlatSpec with ShouldMatchers with EvaluationMatchers {

  import org.omath.symbols.{ SetDelayed, Null, List, Pattern, Blank }

  // TODO these could all use the parser, for readability...
  "SetDelayed" should "evaluate to Null" in {
    TungstenBootstrap.evaluate(SetDelayed('x0, 'y0)) should equal(Null)
  }
  "SetDelayed" should "create an OwnValue" in {
    TungstenBootstrap.evaluate(SetDelayed('x1, 'y1)) should equal(Null)
    TungstenBootstrap.evaluate('x1) should equal(SymbolExpression('y1))
  }
  "SetDelayed" should "create a DownValue" in {
    TungstenBootstrap.evaluate(SetDelayed(SymbolExpression('x2)(Pattern('y2, Blank())), List('y2, 'y2))) should equal(Null)
    TungstenBootstrap.evaluate(SymbolExpression('x2)(2)) should equal(List(2, 2))
  }
  "SetDelayed" should "create a SubValue" in {
    TungstenBootstrap.evaluate(SetDelayed(SymbolExpression('x3)()(), 'y3)) should equal(Null)
    TungstenBootstrap.evaluate(SymbolExpression('x3)()()) should equal(SymbolExpression('y3))
  }
  "SetDelayed" should "hold its arguments" in {
    TungstenBootstrap.evaluate(SetDelayed('y4, 2)) should equal(Null)
    TungstenBootstrap.evaluate(SetDelayed(SymbolExpression('x4)(Pattern('y4, Blank())), List('y4, 'y4))) should equal(Null)
    TungstenBootstrap.evaluate(SymbolExpression('x4)(2)) should equal(List(2, 2))
  }

  "SetDelayed" should "correctly create a SubValue when the head has a typed Blank" in {
    """f_F[x_] := {x, f}
       F[1][2]""" should evaluateTo("{2, F[1]}")
    """g_G[x_][y_] := {g, x, y}
       G[1][2]""" should evaluateTo("G[1][2]")
    "G[1][2][3]" should evaluateTo("{G[1], 2, 3}")
  }

}

