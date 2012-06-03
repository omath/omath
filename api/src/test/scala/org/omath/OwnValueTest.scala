package org.omath

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OwnValueTest extends FlatSpec with ShouldMatchers {

  trait MockKernelState { kernel: Kernel =>
    def kernelState = new EmptyKernelState {
      override def ownValues(symbol: SymbolExpression) = {
        symbol.name match {
          case "x" => ReplacementRule(symbol, 2)
          case _ => ReplacementRuleTable()
        }
      }
    }
  }

  "own values" should "correctly apply" in {
    object K extends Tungsten with MockKernelState
    K.evaluate('x) should equal (IntegerExpression(2))
  }

}