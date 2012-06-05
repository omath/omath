package org.omath.kernel

import org.omath.IntegerExpression
import org.omath.SymbolExpression
import org.omath.patterns.ReplacementRule
import org.omath.patterns.ReplacementRuleTable


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

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

  object K extends Tungsten with MockKernelState

  "absent OwnValues" should "result in nothing happening" in {
    val y: SymbolExpression = 'y
    K.evaluate(y) should equal(y)
  }
  "own values" should "correctly apply" in {
    K.evaluate('x) should equal(IntegerExpression(2))
  }

}