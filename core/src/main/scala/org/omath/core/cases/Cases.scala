package org.omath.core.cases

import org.omath.Expression
import org.omath.FullFormExpression
import org.omath.SymbolExpression
import org.omath.IntExpression
import org.omath.patterns.Pattern
import org.omath.kernel.Evaluation
import org.omath.symbols
import org.omath.patterns.ReplacementRule
import org.omath.core.LevelSpecification
import org.omath.core.Span

object Cases {

  def apply(
    expression: Expression,
    pattern: Expression,
    levelSpecification: LevelSpecification,
    span: Span,
    heads: Boolean)(
      implicit evaluation: Evaluation): Expression = {

    implicit val attributes = evaluation.kernel.kernelState.attributes _

    symbols.List(impl(expression, pattern, levelSpecification.withHeads(heads), span).map(_._1).toSeq: _*)
  }

  def withRule(
    expression: Expression,
    rule: ReplacementRule,
    levelSpecification: LevelSpecification,
    span: Span,
    heads: Boolean)(
      implicit evaluation: Evaluation): Expression = {

    symbols.List(impl(expression, rule.pattern, levelSpecification.withHeads(heads), span).map(p => rule.result.activeBind(p._2)).toSeq: _*)
  }

  private def impl(
    expression: Expression,
    pattern: Pattern,
    levelSpecification: LevelSpecification,
    span: Span)(
      implicit evaluation: Evaluation): Iterator[(Expression, Map[SymbolExpression, Expression])] = {

    val cases = for (
      piece <- levelSpecification.piecesOf(expression);
      matching = pattern.matching(piece) if matching.hasNext
    ) yield (piece, matching.next)

    span.take(cases)
  }
}