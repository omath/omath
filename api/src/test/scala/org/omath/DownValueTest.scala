package org.omath

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DownValueTest extends FlatSpec with ShouldMatchers {

  trait MockKernelState { kernel: Kernel =>
    def kernelState = new EmptyKernelState {
      override def downValues(symbol: SymbolExpression) = {
        symbol.name match {
          case "f" => ReplacementRule(SymbolExpression("f")(2), 4)
          case "g" => ReplacementRule(SymbolExpression("g")(symbols.Blank()), 6)
          case "h" => ReplacementRule(SymbolExpression("h")(symbols.Blank(), symbols.Blank()), 8)
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

}