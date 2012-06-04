package org.omath.patterns

import org.omath.symbols
import org.omath.SymbolExpression

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BlankTest extends PatternTest {

  "Blank" should "match anything" in {
    val pattern: Pattern = symbols.Blank()
    pattern.bind(2).size should equal(1)
  }

  "Blank[h]" should "only match expressions with head h" in {
    val pattern: Pattern = symbols.Blank('h)
    pattern.bind(2).size should equal(0)
    pattern.bind('h).size should equal(0)    
    // FIXME:
    pattern.bind(SymbolExpression("h")()).size should equal(1)    
  }
}