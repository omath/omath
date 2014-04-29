package org.omath.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.omath.expression._
import scala.util.Success

@RunWith(classOf[JUnitRunner])
class SyntaxParserTest extends FlatSpec with Matchers {

  implicit def builder: ExpressionBuilder[Expression] = MockExpressionBuilder

  "SyntaxParser" should "parse expressions" in {
    SyntaxParserImplementation.parseSyntax("a + b") should equal(Success(MockExpression.FullFormExpression(MockExpression.SymbolExpression("Plus"), Seq(MockExpression.SymbolExpression("a"), MockExpression.SymbolExpression("b")))))
  }
}