package org.omath.core

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.util._
import org.omath._
import org.omath.kernel.tungsten.Tungsten
import org.omath.parser.SyntaxParserImplementation
import org.omath.bootstrap.JavaObjectExpression
import org.omath.core.javaObjects.Deserialize
import org.omath.core.javaObjects.Serialize
import org.omath.kernel.KernelState

@RunWith(classOf[JUnitRunner])
class CoreUnitTests extends FlatSpec with ShouldMatchers with EvaluationMatchers {

  // No actual tests are contained in this file. You can create tests directly in Core.m via commands like
  //    CreateUnitTest[Plus, "should be commutative", 1+2 === 2+1]

  import org.omath.symbols

  val UnitTest: Expression = SymbolExpression("UnitTest", "System")
  val $UnitTests: Expression = SymbolExpression("$UnitTests", "System")

  // For additional torture, we instantiate a TungstenCore, then serialize/deserialize its KernelState, _twice_, and run the tests on that!
  val core1 = new TungstenCore { }
  val core2 = new Tungsten with SyntaxParserImplementation {
    override val kernelState = core1.evaluateSyntax("""Deserialize[Serialize[KernelReference[]["kernelState"[]]]]""").get.asInstanceOf[JavaObjectExpression[KernelState]].contents
  }
  implicit val core3 = new Tungsten with SyntaxParserImplementation {
    override val kernelState = core2.evaluateSyntax("""Deserialize[Serialize[KernelReference[]["kernelState"[]]]]""").get.asInstanceOf[JavaObjectExpression[KernelState]].contents
  }

  core3.evaluate($UnitTests) match {
    case FullFormExpression(symbols.List, tests) => {
      for (FullFormExpression(UnitTest, Seq(symbol: SymbolExpression, StringExpression(shouldText), symbols.Hold(expression))) <- tests) {
        (shouldText.split("should").toList match {
          case one :: Nil => symbol.toString should "satisfy: '" + one + "'"
          case one :: rest => (symbol.toString + " " + one.trim) should rest.mkString("should").trim
        }) in {
          implicit val attributes = core3.kernelState.attributes _
          expression match {
            case symbols.MatchQ(expression, pattern) => expression should satisfy(pattern)(core3)
            case symbols.SameQ(expression1, expression2) => expression1 should evaluateTo(expression2)(core3)
            case expression => expression should evaluateTo(symbols.True)(core3)
          }
        }
      }
    }
  }

}

