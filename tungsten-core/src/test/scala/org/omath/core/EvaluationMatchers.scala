package org.omath.core

import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher
import org.omath.Expression
import org.omath.patterns.Pattern
import org.omath.kernel.Kernel

trait EvaluationMatchers {
  implicit val t = TungstenCore

  // FIXME evaluateTo should not evaluate the right hand side
  class StringEvaluationMatcher(right: String)(implicit kernel: Kernel) extends Matcher[String] {
    def apply(left: String) = {
      val leftEvaluation = kernel.evaluateSyntax(left)
      val rightEvaluation = kernel.evaluateSyntax(right)
      MatchResult(
        leftEvaluation == rightEvaluation,
        left + " evaluated to " + leftEvaluation.get.toString + " instead of " + rightEvaluation.get.toString + ".",
        "The strings evaluate to the same result.")
    }
  }
  // FIXME here too
  class ExpressionEvaluationMatcher(right: Expression)(implicit kernel: Kernel) extends Matcher[Expression] {
    def apply(left: Expression) = {
      val leftEvaluation = kernel.evaluate(left)
      val rightEvaluation = kernel.evaluate(right)
      MatchResult(
        leftEvaluation == rightEvaluation,
        left + " evaluated to " + leftEvaluation + " instead of " + rightEvaluation + ".",
        "The expressions evaluate to the same result.")
    }
  }
  class ExpressionEvaluationPatternMatcher(right: Pattern)(implicit kernel: Kernel) extends Matcher[Expression] {
    def apply(left: Expression) = {
      val evaluation = kernel.evaluate(left)
      MatchResult(
        right.matching(evaluation)(kernel.newEvaluation).hasNext,
        left + " evaluated to " + evaluation + " which does not match " + right + ".",
        "The expression evaluates to something matching the pattern.")
    }
  }

  def evaluateTo(s: String)(implicit kernel: Kernel) = new StringEvaluationMatcher(s)
  def evaluateTo(e: Expression)(implicit kernel: Kernel) = new ExpressionEvaluationMatcher(e)
  def satisfy(p: Pattern)(implicit kernel: Kernel) = new ExpressionEvaluationPatternMatcher(p)
}