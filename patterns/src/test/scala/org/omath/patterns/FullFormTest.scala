package org.omath.patterns

import org.omath.symbols
import org.omath.SymbolExpression

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FullFormTest extends PatternTest {

  "empty arguments" should "not break FullFormExpressionPattern" in {
    val pattern1: Pattern = symbols.List()
    pattern1.matching(symbols.List()).size should equal(1)
    val pattern2: Pattern = symbols.List()()
    pattern2.matching(symbols.List()()).size should equal(1)
  }

  "h[_]" should "match h[2]" in {
    val h: SymbolExpression = 'h
    val pattern: Pattern = h(symbols.Blank())
    pattern.matching(h(2)).size should equal(1)
  }

  "blanks in the head" should "work" in {
    val pattern: Pattern = symbols.Blank()(symbols.BlankNullSequence())
    pattern.matching(symbols.List()).size should equal(1)
  }
}