package org.omath.patterns

import org.omath.symbols
import org.omath.Expression
import org.omath.SymbolExpression
import org.omath.IntegerExpression

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class NamedPatternTest extends PatternTest {

  "Pattern" should "bind the result" in {
    val pattern: Pattern = symbols.Pattern('x, symbols.Blank())
    val binding = pattern.bind(2).toSeq
    binding.size should equal(1)
    binding.head should equal(Map(SymbolExpression("x") -> IntegerExpression(2)))
  }

  "repeated Patterns" should "only bind if the value is the same" in {
    val pattern: Pattern = symbols.List(symbols.Pattern('x, symbols.Blank()), symbols.Pattern('x, symbols.Blank()))
    pattern.bind(symbols.List(2, 2)).size should equal(1)
    pattern.bind(symbols.List(2, 3)).size should equal(0)
  }

}