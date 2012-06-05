package org.omath.kernel

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

import org.omath.IntegerExpression
import org.omath.SymbolExpression
import org.omath.patterns.ReplacementRule
import org.omath.patterns.ReplacementRuleTable

import org.omath.symbols.{ Pattern, Blank, List }


@RunWith(classOf[JUnitRunner])
class DownValueTest extends FlatSpec with ShouldMatchers {

  trait MockKernelState { kernel: Kernel =>
    def kernelState = new EmptyKernelState {
      override def downValues(symbol: SymbolExpression) = {
        symbol.name match {
          case "f" => ReplacementRule(SymbolExpression("f")(2), 4)
          case "g" => ReplacementRule(SymbolExpression("g")(Blank()), 6)
          case "h" => ReplacementRule(SymbolExpression("h")(Blank(), Blank()), 8)
          case "k" => ReplacementRule(SymbolExpression("k")(Pattern('x, Blank())), List('x, 'x))
          case _ => ReplacementRuleTable()
        }
      }
    }
  }

  object K extends Tungsten with MockKernelState

  "down values" should "correctly apply to a literal" in {
    K.evaluate(SymbolExpression("f")(2)) should equal(IntegerExpression(4))
  }
  "down values containing a blank" should "correctly apply to a literal" in {
    K.evaluate(SymbolExpression("g")(2)) should equal(IntegerExpression(6))
  }
  "down values containing multiple blanks" should "correctly apply to literals" in {
    K.evaluate(SymbolExpression("h")(2, 3)) should equal(IntegerExpression(8))
  }
  "named blanks" should "result in a substitution on the right hand side" in {
    K.evaluate(SymbolExpression("k")(5, 5)) should equal(SymbolExpression("k")(5, 5))
    K.evaluate(SymbolExpression("k")(6)) should equal(List(6, 6))
  }

}