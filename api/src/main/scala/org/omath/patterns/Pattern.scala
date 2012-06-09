package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

// TODO PartialBinding is an implementation detail!
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
  def matching(expressions: Expression*)(implicit evaluation: Evaluation): Iterator[Map[SymbolExpression, Expression]] = {
    extend(Map.empty[SymbolExpression, Expression])(expressions: _*)
  }
}

object Pattern {
  var patternBuilder: Expression => ExpressionPattern = { _ => throw new Exception("The patternBuilder field of the Pattern object must be initialized before Expressions can be converted into Patterns. Probably you forgot to mention the PatternBuilder object in the patterns subproject.") }

  import language.implicitConversions
  implicit def expression2Pattern(e: Expression): ExpressionPattern = patternBuilder(e)

  def compose(patterns: Pattern*): Pattern = {
    patterns match {
      case Seq() => {
        new Pattern {
          override def pure = true
          override def extend(a: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding] = {
            Iterator(a.copy(lastBound = Seq()))
          }
        }
      }
      case h +: Nil => h
      case patterns => {
        case class PairPattern(first: Pattern, second: Pattern) extends Pattern {
          override lazy val pure = first.pure && second.pure
          override def extend(a: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding] = {
            for (b <- first.extend(a); c <- second.extend(b)) yield c.copy(lastBound = b.lastBound ++ c.lastBound)
          }
        }
        patterns.reduce(PairPattern(_, _))
      }
    }
  }
}

trait ExpressionPattern extends Pattern {
  def expression: Expression
  override def toString = expression.toString
}


