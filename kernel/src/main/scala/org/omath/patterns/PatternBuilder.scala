package org.omath.patterns

import org.omath._

object PatternBuilder {
  Pattern.patternBuilder = apply _
  
  def apply(e: Expression): ExpressionPattern = {
    e match {
      case e: RawExpression => RawExpressionPattern(e)
      case FullFormExpression(symbols.Blank, Nil) => BlankPattern
      case e @ FullFormExpression(symbols.Blank, (n: SymbolExpression) :: Nil) => NamedPattern(e, n, BlankPattern)
      // TODO a lot more here!
      case e: FullFormExpression => FullFormExpressionPattern(e, apply(e.head), Pattern.compose(e.arguments.map(apply): _*))
    }
  }
}

case class AlternativePattern(alternatives: Pattern*) extends Pattern {
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding] = {
    for (x <- alternatives.iterator; b <- x.extend(a)) yield b
  }
}

case class RawExpressionPattern(override val expression: RawExpression) extends ExpressionPattern {
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    // TODO clean this up once there's a proper Seq extractor
    (if (a.remainingExpressions.isEmpty) {
      None
    } else {
      a.remainingExpressions.head match {
        case h: RawExpression if h == expression => Some(PartialBinding(a.binding, a.remainingExpressions.tail, Seq(h)))
        case _ => None
      }
    }).iterator
  }
}
case class NamedPattern(override val expression: FullFormExpression, name: SymbolExpression, inner: Pattern) extends ExpressionPattern {
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    for (
      PartialBinding(binding, remaining, last) <- inner.extend(a);
      lastSequence = symbols.Sequence(last: _*);
      if (binding.get(name) match {
        case None => true
        case Some(e) if e == lastSequence => true
        case _ => false
      })
    ) yield PartialBinding(binding + (name -> lastSequence), remaining, last)
  }
}
case object BlankPattern extends ExpressionPattern {
  override val expression = symbols.Blank()
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    // TODO once there's head/tail extractor for Seq, clean this up.
    (if (a.remainingExpressions.isEmpty) {
      None
    } else {
      Some(a.copy(remainingExpressions = a.remainingExpressions.tail, lastBound = Seq(a.remainingExpressions.head)))
    }).iterator
  }
}
case class FullFormExpressionPattern(override val expression: FullFormExpression, headPattern: Pattern, argumentPattern: Pattern) extends ExpressionPattern {
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    a match {
      case PartialBinding(b1, expressions, _) => {
        // TODO once there's head/tail extractor for Seq, clean this up.
        if (expressions.isEmpty) {
          Nil.iterator
        } else {
          val x = expressions.head
          x match {
            case x: FullFormExpression => {
              for (b2 <- headPattern.extend(b1)(x.head); b3 <- argumentPattern.extend(b2)(x.arguments: _*)) yield {
                PartialBinding(b3, expressions.tail, Seq(x))
              }
            }
            case _ => Nil.iterator
          }
        }
      }
    }
  }
}
