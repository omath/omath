package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

// TODO PartialBinding is an implementation detail!
case class PartialBinding(binding: Map[SymbolExpression, Expression], remainingExpressions: Seq[Expression], lastBound: Seq[Expression])

trait Pattern extends Serializable {
  def pure: Boolean

  def extend(partialBinding: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding]
  def extend(binding: Map[SymbolExpression, Expression])(expressions: Expression*)(implicit evaluation: Evaluation): Iterator[Map[SymbolExpression, Expression]] = {
    extend(PartialBinding(binding, expressions, Nil)).collect({
      case PartialBinding(b, Nil, last) => {
        b
      }
    })
  }
  def matching(expressions: Expression*)(implicit evaluation: Evaluation): Iterator[Map[SymbolExpression, Expression]] = {
    extend(Map.empty[SymbolExpression, Expression])(expressions: _*)
  }
  def matches(expressions: Expression*)(implicit evaluation: Evaluation) = matching(expressions: _*)(evaluation).hasNext
}

object Pattern extends PartialOrdering[Pattern] {
  var patternBuilder: Expression => ((SymbolExpression => Seq[SymbolExpression]) => ExpressionPattern) = { _ => throw new Exception("The patternBuilder field of the Pattern object must be initialized before Expressions can be converted into Patterns. Probably you forgot to mention the PatternBuilder object in the patterns subproject.") }
  var patternComparator: (Pattern, Pattern) => Option[Int] = { (_, _) => throw new Exception("The patternComparator field of the Pattern object must be initialized before tables of rules can be built.") }
  
  import org.omath.util.Scala29Compatibility._
  import language.implicitConversions
  implicit def expression2Pattern(e: Expression)(implicit attributes: SymbolExpression => Seq[SymbolExpression]): ExpressionPattern = patternBuilder(e)(attributes)
  
  // Should this line by on PartialOrdering?
  override def lteq(x: Pattern, y: Pattern) = tryCompare(x, y).map(_ <= 0).getOrElse(false)
  override def tryCompare(x: Pattern, y: Pattern) = patternComparator(x, y)
  
  object Empty extends Pattern {
    override def pure = true
    override def extend(a: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding] = {
      Iterator(a.copy(lastBound = Seq()))
    }
  }

  def compose(patterns: Pattern*): Pattern = {
    patterns match {
      case Seq() => Empty
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


