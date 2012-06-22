package org.omath.patterns

import org.omath._

object PatternBuilder {
  Pattern.patternBuilder = { e: Expression => { attributes: (SymbolExpression => Seq[SymbolExpression]) => apply(e)(attributes) } }

  def apply(e: Expression)(implicit attributes: SymbolExpression => Seq[SymbolExpression]): ExpressionPattern = {
    e match {
      case e: RawExpression => RawExpressionPattern(e)
      case symbols.Blank() => Blank(None)
      case symbols.Blank(head: SymbolExpression) => Blank(Some(head))
      case symbols.BlankSequence() => BlankSequence(None)
      case symbols.BlankSequence(head: SymbolExpression) => BlankSequence(Some(head))
      case symbols.BlankNullSequence() => BlankNullSequence(None)
      case symbols.BlankNullSequence(head: SymbolExpression) => BlankNullSequence(Some(head))
      case _ => e match { // we need to split up the match because of https://issues.scala-lang.org/browse/SI-1133 (fixed in 2.10.0)
        case e @ symbols.Pattern(n: SymbolExpression, x) => NamedPattern(e, apply(x))
        case FullFormExpression(symbols.Pattern, _) => throw new PatternException(e)
        case symbols.HoldPattern(p) => HoldPattern(apply(p))
        case FullFormExpression(symbols.HoldPattern, _) => throw new PatternException(e)
        case e @ FullFormExpression(symbols.Alternatives, arguments) => AlternativesPattern(e, arguments.map(apply): _*)
        case symbols.Condition(pattern, condition) => ConditionPattern(apply(pattern), condition)
        case symbols.Repeated(pattern) => RepeatedPattern(apply(pattern))
        case symbols.RepeatedNull(pattern) => RepeatedNullPattern(apply(pattern))
        // TODO a lot more here!
        case e: FullFormExpression => FullFormExpressionPattern(e, apply(e.head), Pattern.compose(e.arguments.map(apply): _*))
      }
    }
  }

  private class PatternException(e: Expression) extends Exception("Invalid pattern expression: " + e.toString)
}

