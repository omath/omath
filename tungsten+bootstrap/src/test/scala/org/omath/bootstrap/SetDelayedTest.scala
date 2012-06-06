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
class SetDelayedTest extends FlatSpec with ShouldMatchers {

  import org.omath.symbols.{ SetDelayed, Null, List, Pattern, Blank }
  
  "SetDelayed" should "evaluate to Null" in {
	  TungstenBootstrap.evaluate(SetDelayed('x0, 'y0)) should equal(Null)
  }
  "SetDelayed" should "create an OwnValue" in {
	  TungstenBootstrap.evaluate(SetDelayed('x1, 'y1)) should equal(Null)
	  TungstenBootstrap.evaluate('x1) should equal(SymbolExpression('y1))
  }
  "SetDelayed" should "create an DownValue" in {
	  TungstenBootstrap.evaluate(SetDelayed(SymbolExpression('x2)(Pattern('y2, Blank())), List('y2, 'y2))) should equal(Null)
	  TungstenBootstrap.evaluate(SymbolExpression('x2)(2)) should equal(List(2, 2))
  }

}