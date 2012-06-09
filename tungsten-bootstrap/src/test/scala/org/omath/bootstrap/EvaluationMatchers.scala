package org.omath.bootstrap

import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher

trait EvaluationMatchers {
  class StringEvaluationMatcher(right: String) extends Matcher[String] {
    def apply(left: String) = {
      val leftEvaluation = TungstenBootstrap.evaluateSyntax(left)
      val rightEvaluation = TungstenBootstrap.evaluateSyntax(right)
      MatchResult(
        leftEvaluation == rightEvaluation,
        left + " evaluated to " + leftEvaluation.get.toString + " instead of " + rightEvaluation.get.toString,
        "The strings evaluate to the same expression")
    }
  }

  def evaluateTo(s: String) = new StringEvaluationMatcher(s)
}