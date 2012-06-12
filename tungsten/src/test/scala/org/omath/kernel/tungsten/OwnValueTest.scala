package org.omath.kernel.tungsten

import org.omath.IntegerExpression
import org.omath.SymbolExpression

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

@RunWith(classOf[JUnitRunner])
class OwnValueTest extends FlatSpec with ShouldMatchers {

  "absent OwnValues" should "result in nothing happening" in {
    val y: SymbolExpression = 'y
    MockTungsten.evaluate(y) should equal(y)
  }
  "own values" should "correctly apply" in {
    MockTungsten.evaluate('x) should equal(IntegerExpression(2))
  }

}