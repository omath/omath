package org.omath.patterns

import org.omath.symbols
import org.omath.SymbolExpression

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BlankTest extends PatternTest {

  "Blank" should "match anything" in {
    val pattern: Pattern = symbols.Blank()
    pattern.matching(2).size should equal(1)
  }

  "Blank[h]" should "only match expressions with head h" in {
    val pattern: Pattern = symbols.Blank('h)
    pattern.matching(2).size should equal(0)
    pattern.matching('h).size should equal(0)
    pattern.matching(SymbolExpression("h")()).size should equal(1)
  }

  "Blank[Integer]" should "only match integers" in {
    val pattern: Pattern = symbols.Blank(symbols.Integer)
    pattern.matching(2).size should equal(1)
    pattern.matching(symbols.Integer).size should equal(0)
    pattern.matching(SymbolExpression("h")()).size should equal(0)
  }

  "BlankSequence" should "match any nonempty sequence" in {
    val pattern: Pattern = symbols.BlankSequence()
    pattern.matching(2).size should equal(1)
    pattern.matching(2, 2).size should equal(1)
    pattern.matching(2, 'x).size should equal(1)
    pattern.matching().size should equal(0)
  }
  "BlankSequence[Integer]" should "match any nonempty sequence of Integers" in {
    val pattern: Pattern = symbols.BlankSequence(symbols.Integer)
    pattern.matching(2).size should equal(1)
    pattern.matching(2, 2).size should equal(1)
    pattern.matching(2, 'x).size should equal(0)
    pattern.matching().size should equal(0)
  }

  "BlankNullSequence" should "match any  sequence" in {
    val pattern: Pattern = symbols.BlankNullSequence()
    pattern.matching(2).size should equal(1)
    pattern.matching(2, 2).size should equal(1)
    pattern.matching(2, 'x).size should equal(1)
    pattern.matching().size should equal(1)
  }

  "multiple BlankSequences" should "match the correct number of times" in {
    val pattern: Pattern = symbols.List(symbols.BlankSequence(), symbols.BlankSequence())
    pattern.matching(symbols.List(2)).size should equal(0)
    pattern.matching(symbols.List(2, 2)).size should equal(1)
    pattern.matching(symbols.List(2, 2, 2)).size should equal(2)
  }
  
  "multiple BlankNullSequences" should "match the correct number of times" in {
    val pattern2: Pattern = symbols.List(symbols.BlankNullSequence(), symbols.BlankNullSequence())
    pattern2.matching(symbols.List(2)).size should equal(2)
    pattern2.matching(symbols.List(2, 2)).size should equal(3)
    pattern2.matching(symbols.List(2, 2, 2)).size should equal(4)
    val pattern3: Pattern = symbols.List(symbols.BlankNullSequence(), symbols.BlankNullSequence(), symbols.BlankNullSequence())
    pattern3.matching(symbols.List(2, 2, 2)).size should equal(10)
  }
  
  "combinations of Blanks, BlankSequences and BlankNullSequences" should "match the correct number of times" in {
    val pattern: Pattern = symbols.List(symbols.BlankNullSequence(), symbols.Blank(), symbols.BlankSequence())
    pattern.matching(symbols.List(2)).size should equal(0)
    pattern.matching(symbols.List(2, 2)).size should equal(1)
    pattern.matching(symbols.List(2, 2, 2)).size should equal(2)
    val pattern2: Pattern = symbols.List(symbols.BlankNullSequence(), symbols.Blank(symbols.Integer), symbols.BlankSequence())
    pattern2.matching(symbols.List('x, 'x, 2, 'x)).size should equal(1)
    pattern2.matching(symbols.List('x, 2, 2, 'x)).size should equal(2)
  }

}