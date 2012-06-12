package org.omath.kernel.tungsten

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

import org.omath.IntegerExpression
import org.omath.SymbolExpression



@RunWith(classOf[JUnitRunner])
class DownValueTest extends FlatSpec with ShouldMatchers {


  "down values" should "correctly apply to a literal" in {
    MockTungsten.evaluate(SymbolExpression("f")(2)) should equal(IntegerExpression(4))
  }
  "down values containing a blank" should "correctly apply to a literal" in {
    MockTungsten.evaluate(SymbolExpression("g")(2)) should equal(IntegerExpression(6))
  }
  "down values containing multiple blanks" should "correctly apply to literals" in {
    MockTungsten.evaluate(SymbolExpression("h")(2, 3)) should equal(IntegerExpression(8))
  }
  "named blanks" should "result in a substitution on the right hand side" in {
    MockTungsten.evaluate(SymbolExpression("k")(5, 5)) should equal(SymbolExpression("k")(5, 5))
    MockTungsten.evaluate(SymbolExpression("k")(6)) should equal(org.omath.symbols.List(6, 6))
  }

}