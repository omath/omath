package org.omath.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.SymbolExpression
import org.omath.util._

@RunWith(classOf[JUnitRunner])
class SyntaxParserTest extends FlatSpec with ShouldMatchers {

  implicit val symbolizer = { s: String => SymbolExpression(s) }
  
  "SyntaxParser" should "parse expressions" in {
    SyntaxParser("a + b") should equal(Success(SymbolExpression("Plus")(SymbolExpression("a"), SymbolExpression("b"))))
  }
}