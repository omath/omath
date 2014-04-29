package org.omath.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.omath.expression._
import org.omath.expression.MockExpressionBuilder
import scala.util.Success

@RunWith(classOf[JUnitRunner])
class FullFormParserTest extends FlatSpec with Matchers {

  implicit def builder = MockExpressionBuilder
  
  "FullFormParser" should "return Null on whitespace only inputs" in {
    FullFormParser("") should equal(Success(MockExpression.SymbolExpression("Null")))
    FullFormParser(" ") should equal(Success(MockExpression.SymbolExpression("Null")))
  }
  "FullFormParser" should "parse integers to IntegerExpressions that are just like we construct in code" in {
    FullFormParser("1") should equal(Success(MockExpression.IntegerExpression("1")))
  }
  "FullFormParser" should "parse decimals to RealExpressions that are just like we construct in code" in {
    FullFormParser("1.1") should equal(Success(MockExpression.RealExpression("1.1")))
  }
  "FullFormParser" should "parse expressions" in {
    FullFormParser("a[b]") should equal(Success(MockExpression.FullFormExpression(MockExpression.SymbolExpression("a"), Seq(MockExpression.SymbolExpression("b")))))
  }
  
  "FullFormParser" should "not mess up quoting of strings" in {
    FullFormParser("\"a\"").get.toString should equal("\"a\"")
  }

  "FullFormParser" should "not insert spurious Nulls in empty argument lists" in {
    FullFormParser("f[]").get.toString should equal("f[]")
  }

}