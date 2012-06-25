package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class NamedPattern(override val expression: FullFormExpression, val inner: ExpressionPattern) extends ExpressionPattern {
  def name = expression.arguments(0).asInstanceOf[SymbolExpression]
  override def pure = inner.pure
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

