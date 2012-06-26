package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

// TODO PartialBinding is an implementation detail!
case class PartialBinding(binding: Map[SymbolExpression, Expression], remainingExpressions: Seq[Expression], lastBound: Seq[Expression])

trait Pattern extends Serializable {
  def pure: Boolean

  def names: Seq[SymbolExpression]

  def asExpression: Expression
  override def toString = asExpression.toString

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

  def :>(bindable: Bindable) = ReplacementRule(this, bindable)
  
  def evaluateArguments(implicit evaluation: Evaluation): Expression = {
    implicit val attributes = evaluation.kernel.kernelState.attributes _

    this.asExpression match {
      case symbols.Condition(pattern, condition) => symbols.Condition(Pattern.expression2Pattern(pattern).evaluateArguments, condition)
      case FullFormExpression(head, arguments) => head(arguments.map(a => evaluation.evaluate(a)): _*)
      case other => other
    }
  }
}

case class PairPattern(first: Pattern, second: Pattern) extends Pattern {
  override lazy val pure = first.pure && second.pure
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding] = {
    for (b <- first.extend(a); c <- second.extend(b)) yield c.copy(lastBound = b.lastBound ++ c.lastBound)
  }
  override def names = first.names ++ second.names
  override def asExpression = symbols.Sequence((first.asExpression match {
    case FullFormExpression(symbols.Sequence, fs) => fs
    case f => Seq(f)
  }) ++ (second.asExpression match {
    case FullFormExpression(symbols.Sequence, fs) => fs
    case f => Seq(f)
  }): _*)
}

object Pattern extends PartialOrdering[Pattern] {
  var patternBuilder: Expression => ((SymbolExpression => Seq[SymbolExpression]) => Pattern) = { _ => throw new Exception("The patternBuilder field of the Pattern object must be initialized before Expressions can be converted into Patterns. Probably you forgot to mention the PatternBuilder object in the patterns subproject.") }
  var patternComparator: (Pattern, Pattern) => Option[Int] = { (_, _) => throw new Exception("The patternComparator field of the Pattern object must be initialized before tables of rules can be built.") }

  import org.omath.util.Scala29Compatibility._
  import language.implicitConversions
  implicit def expression2Pattern(e: Expression)(implicit attributes: SymbolExpression => Seq[SymbolExpression]): Pattern = patternBuilder(e)(attributes)

  // Should this line by on PartialOrdering?
  override def lteq(x: Pattern, y: Pattern) = tryCompare(x, y).map(_ <= 0).getOrElse(false)
  override def tryCompare(x: Pattern, y: Pattern) = patternComparator(x, y)

  object Empty extends Pattern {
    override def pure = true
    override def asExpression = org.omath.symbols.Sequence()
    override def extend(a: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding] = {
      Iterator(a.copy(lastBound = Seq()))
    }
    override def names = Seq.empty
  }

  def compose(patterns: Pattern*): Pattern = {
    patterns match {
      case Seq() => Empty
      case h +: Nil => h
      case patterns => {
        patterns.reduce(PairPattern(_, _))
      }
    }
  }
  
  def unwrap(expression: Expression): Expression = {
    expression match {
      case symbols.Condition(pattern, _) => unwrap(pattern)
      case symbols.Pattern(_, pattern) => unwrap(pattern)
      case other => other
  }

  }
}


