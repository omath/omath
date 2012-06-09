package org.omath.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.SymbolExpression
import org.omath.util._
import org.omath.symbols

@RunWith(classOf[JUnitRunner])
class FullFormParserTest extends FlatSpec with ShouldMatchers {

  implicit val symbolizer = { s: String => SymbolExpression(s) }
  
  "FullFormParser" should "return Null on whitespace only inputs" in {
    FullFormParser("") should equal(Success(symbols.Null))
//    FullFormParser(" ") should equal(Success(symbols.Null))
  }
  "FullFormParser" should "parse expressions" in {
    FullFormParser("a[b]") should equal(Success(SymbolExpression("a")(SymbolExpression("b"))))
  }
  
  "FullFormParser" should "not mess up quoting of strings" in {
    FullFormParser("\"a\"").get.toString should equal("\"a\"")
  }

  "FullFormParser" should "not insert spurious Nulls in empty argument lists" in {
    FullFormParser("f[]").get.toString should equal("f[]")
  }

}