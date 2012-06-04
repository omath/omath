package org.omath.patterns

import org.omath._

case class PartialBinding(binding: Map[SymbolExpression, Expression], remainingExpressions: Seq[Expression], lastBound: Seq[Expression])

trait Pattern {
  def pure: Boolean
  
  def extend(partialBinding: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding]
  def extend(binding: Map[SymbolExpression, Expression])(expressions: Expression*)(implicit evaluation: Evaluation): Iterator[Map[SymbolExpression, Expression]] = {
    extend(PartialBinding(binding, expressions, Nil)).collect({
      case PartialBinding(b, Nil, last) => {
        require(last == expressions)
        b
      }
    })
  }
  def bind(expressions: Expression*)(implicit evaluation: Evaluation): Iterator[Map[SymbolExpression, Expression]] = {
    extend(Map[SymbolExpression, Expression]())(expressions: _*)
  }
}


object Pattern {
  var patternBuilder: Expression => ExpressionPattern = { _ => ??? }
  
  implicit def expression2Pattern(e: Expression): ExpressionPattern = patternBuilder(e)

  def compose(patterns: Pattern*): Pattern = {
    case class PairPattern(first: Pattern, second: Pattern) extends Pattern {
      override lazy val pure = first.pure && second.pure
      override def extend(a: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding] = {
        for (b <- first.extend(a); c <- second.extend(b)) yield c.copy(lastBound = b.lastBound ++ c.lastBound)
      }
    }
    patterns.reduce(PairPattern(_, _))
  }
}

trait ExpressionPattern extends Pattern {
  def expression: Expression
}


