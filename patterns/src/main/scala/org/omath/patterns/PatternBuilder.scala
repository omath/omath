package org.omath.patterns

import org.omath._

object PatternBuilder {
  Pattern.patternBuilder = { e: Expression => { attributes: (SymbolExpression => Seq[SymbolExpression]) => apply(e)(attributes) }}

  def apply(e: Expression)(implicit attributes: SymbolExpression => Seq[SymbolExpression]): ExpressionPattern = {
    e match {
      case e: RawExpression => RawExpressionPattern(e)
      case FullFormExpression(symbols.Blank, Seq()) => Blank(None)
      case FullFormExpression(symbols.Blank, Seq(head: SymbolExpression)) => Blank(Some(head))
      case FullFormExpression(symbols.BlankSequence, Seq()) => BlankSequence(None)
      case FullFormExpression(symbols.BlankSequence, Seq(head: SymbolExpression)) => BlankSequence(Some(head))
      case FullFormExpression(symbols.BlankNullSequence, Seq()) => BlankNullSequence(None)
      case FullFormExpression(symbols.BlankNullSequence, Seq(head: SymbolExpression)) => BlankNullSequence(Some(head))
      case e @ FullFormExpression(symbols.Pattern, Seq(n: SymbolExpression, x)) => NamedPattern(e, apply(x))
      case FullFormExpression(symbols.Pattern, _) => throw new PatternException(e)
      case FullFormExpression(symbols.HoldPattern, Seq(p)) => HoldPattern(apply(p))
      case FullFormExpression(symbols.HoldPattern, _) => throw new PatternException(e)
      case e @ FullFormExpression(symbols.Alternatives, arguments) => AlternativesPattern(e, arguments.map(apply):_*)
//      case FullFormExpression(symbols.Condition, Seq(pattern, condition)) =>
      // TODO a lot more here!
      case e: FullFormExpression => FullFormExpressionPattern(e, apply(e.head), Pattern.compose(e.arguments.map(apply): _*))
    }
  }
  
  private class PatternException(e: Expression) extends Exception("Invalid pattern expression: " + e.toString)
}

