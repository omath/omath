package org.omath.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.SymbolExpression

@RunWith(classOf[JUnitRunner])
class FullFormParserTest extends FlatSpec with ShouldMatchers {

  implicit val symbolizer = { s: String => SymbolExpression(s) }
  
  "FullFormParser" should "parse expressions" in {
    FullFormParser("a[b]") should equal(Left(SymbolExpression("a")(SymbolExpression("b"))))
  }
  
  "FullFormParser" should "not mess up quoting of strings" in {
    FullFormParser("\"a\"").left.get.toString should equal("\"a\"")
  }
}