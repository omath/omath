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
    pattern.bind(SymbolExpression("h")()).size should equal(1)    
  }

  "BlankSequence" should "match any nonempty sequence" in {
    val pattern: Pattern = symbols.BlankSequence()
    pattern.bind(2).size should equal(1)
//    pattern.bind(2, 2).size should equal(1)
//    pattern.bind(2, 'x).size should equal(1)
//    pattern.bind().size should equal(0)
  }
}