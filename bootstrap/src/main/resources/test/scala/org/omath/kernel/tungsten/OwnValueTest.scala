package org.omath.kernel.tungsten

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
class OwnValueTest extends FlatSpec with Matchers {

  trait MockKernelState { kernel: Kernel =>
    def kernelState = new EmptyKernelState {
      override def ownValues(symbol: SymbolExpression) = {
        symbol.name match {
          case "x" => ReplacementRule(symbol, 2)
          case _ => ReplacementRuleTable(Nil)
        }
      }
    }
  }

//  FIXME there's apparently some dependency issue here; I'm not sure how to resolve it. 2017-06-08  
//  object K extends Tungsten with MockKernelState
//
//  "absent OwnValues" should "result in nothing happening" in {
//    val y: SymbolExpression = 'y
//    K.evaluate(y) should equal(y)
//  }
//  "own values" should "correctly apply" in {
//    K.evaluate('x) should equal(IntegerExpression(2))
//  }

}