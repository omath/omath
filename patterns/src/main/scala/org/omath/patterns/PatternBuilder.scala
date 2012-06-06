package org.omath.patterns

import org.omath._

object PatternBuilder {
  Pattern.patternBuilder = apply _

  def apply(e: Expression): ExpressionPattern = {
    e match {
      case e: RawExpression => RawExpressionPattern(e)
      case FullFormExpression(symbols.Blank, Nil) => Blank(None)
      case FullFormExpression(symbols.Blank, (head: SymbolExpression) :: Nil) => Blank(Some(head))
      case FullFormExpression(symbols.BlankSequence, Nil) => BlankSequence(None)
      case FullFormExpression(symbols.BlankSequence, (head: SymbolExpression) :: Nil) => BlankSequence(Some(head))
      case FullFormExpression(symbols.BlankNullSequence, Nil) => BlankNullSequence(None)
      case FullFormExpression(symbols.BlankNullSequence, (head: SymbolExpression) :: Nil) => BlankNullSequence(Some(head))
      case e @ FullFormExpression(symbols.Pattern, (n: SymbolExpression) :: x :: Nil) => NamedPattern(e)
      case FullFormExpression(symbols.Pattern, _) => throw new PatternException(e)
      case e @ FullFormExpression(symbols.Alternatives, arguments) => AlternativesPattern(e, arguments.map(apply):_*)
      // TODO a lot more here!
      case e: FullFormExpression => FullFormExpressionPattern(e, apply(e.head), Pattern.compose(e.arguments.map(apply): _*))
    }
  }
  
  private class PatternException(e: Expression) extends Exception("Invalid pattern expression: " + e.toString)
}

