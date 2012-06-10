package org.omath.core

import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher
import org.omath.Expression
import org.omath.patterns.Pattern

trait EvaluationMatchers {
  // FIXME evaluateTo should not evaluate the right hand side
  class StringEvaluationMatcher(right: String) extends Matcher[String] {
    def apply(left: String) = {
      val leftEvaluation = TungstenCore.evaluateSyntax(left)
      val rightEvaluation = TungstenCore.evaluateSyntax(right)
      MatchResult(
        leftEvaluation == rightEvaluation,
        left + " evaluated to " + leftEvaluation.get.toString + " instead of " + rightEvaluation.get.toString + ".",
        "The strings evaluate to the same result.")
    }
  }
  // FIXME here too
  class ExpressionEvaluationMatcher(right: Expression) extends Matcher[Expression] {
    def apply(left: Expression) = {
      val leftEvaluation = TungstenCore.evaluate(left)
      val rightEvaluation = TungstenCore.evaluate(right)
      MatchResult(
        leftEvaluation == rightEvaluation,
        left + " evaluated to " + leftEvaluation + " instead of " + rightEvaluation + ".",
        "The expressions evaluate to the same result.")
    }
  }
  class ExpressionEvaluationPatternMatcher(right: Pattern) extends Matcher[Expression] {
    def apply(left: Expression) = {
      val evaluation = TungstenCore.evaluate(left)
      MatchResult(
        right.matching(evaluation)(TungstenCore.newEvaluation).hasNext,
        left + " evaluated to " + evaluation + " which does not match " + right + ".",
        "The expression evaluates to something matching the pattern.")
    }
  }

  def evaluateTo(s: String) = new StringEvaluationMatcher(s)
  def evaluateTo(e: Expression) = new ExpressionEvaluationMatcher(e)
  def satisfy(p: Pattern) = new ExpressionEvaluationPatternMatcher(p)
}