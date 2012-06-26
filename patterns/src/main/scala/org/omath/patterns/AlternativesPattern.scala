package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class AlternativesPattern(alternatives: Pattern*) extends Pattern {
  override def asExpression = symbols.Alternatives(alternatives.map(_.asExpression):_*)
  override lazy val pure = alternatives.foldLeft(true)(_ && _.pure)
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation): Iterator[PartialBinding] = {
    for (x <- alternatives.iterator; b <- x.extend(a)) yield b
  }
  override def names = alternatives.flatMap(_.names)
}

