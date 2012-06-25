package org.omath.patterns

import org.omath._

object PatternBuilder extends PatternComparator {
  Pattern.patternBuilder = { e: Expression => { attributes: (SymbolExpression => Seq[SymbolExpression]) => apply(e)(attributes) } }
  Pattern.patternComparator = tryCompare _

  def apply(e: Expression)(implicit attributes: SymbolExpression => Seq[SymbolExpression]): ExpressionPattern = {
    import org.omath.util.Scala29Compatibility.???

    // TODO throw more exceptions?
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
        case symbols.PatternTest(pattern, test) => ??? // PatternTestPattern(apply(pattern), test)
        case _ => e match {
          case symbols.Repeated(pattern) => RepeatedPattern(apply(pattern))
          case symbols.RepeatedNull(pattern) => RepeatedNullPattern(apply(pattern))
          case symbols.Optional(pattern, default) => OptionalPattern(apply(pattern), default)
          case FullFormExpression(symbols.PatternSequence, patterns) => ??? // PatternSequence(...)
          case symbols.Except(pattern) => ??? // ExceptPattern(apply(pattern))
          case symbols.Longest(pattern) => ??? // LongestPattern(apply(pattern))
          case symbols.Shortest(pattern) => ??? // ShortestPattern(apply(pattern))
          case symbols.Verbatim(pattern) => ??? // VerbatimPattern(apply(pattern))
          case symbols.OptionsPattern() => OptionsPatternPattern
          case e: FullFormExpression => FullFormExpressionPattern(e, apply(e.head), Pattern.compose(e.arguments.map(apply): _*))
        }
      }
    }
  }

  private class PatternException(e: Expression) extends Exception("Invalid pattern expression: " + e.toString)
}

