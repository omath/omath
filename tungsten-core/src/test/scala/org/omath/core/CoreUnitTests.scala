package org.omath.core

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.util._
import org.omath._

@RunWith(classOf[JUnitRunner])
class CoreUnitTests extends FlatSpec with ShouldMatchers with EvaluationMatchers {

  // No actual tests are contained in this file. You can create tests directly in core.m via commands like
  //    CreateUnitTest[Plus, "should be commutative", 1+2 === 2+1]
  
  import org.omath.symbols
  // FIXME these shouldn't be System symbols
  val UnitTest: Expression = SymbolExpression("UnitTest", "System")
  val $UnitTests: Expression = SymbolExpression("$UnitTests", "System")
  
	TungstenCore.evaluate($UnitTests) match {
	  case FullFormExpression(symbols.List, tests) => {
	    for(FullFormExpression(UnitTest, Seq(symbol: SymbolExpression, StringExpression(shouldText), symbols.Hold(expression))) <- tests) {
	      symbol.toString should (shouldText.stripPrefix("should ")) in {
	        expression match {
	          case symbols.MatchQ(expression, pattern) => expression should satisfy(pattern)
	          case symbols.SameQ(expression1, expression2) => expression1 should evaluateTo(expression2)
	          case expression => expression should evaluateTo(symbols.True)
	        }
	      }
	    }
	  }
	}
  
}

