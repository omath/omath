package org.omath.core

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.util._
import org.omath.StringExpression
import org.omath.SymbolExpression

@RunWith(classOf[JUnitRunner])
class TungstenCoreTest extends FlatSpec with ShouldMatchers with EvaluationMatchers {

  "$Version" should "correctly evaluate, after being set in core.m" in {
    "$Version" should evaluateTo ("\"0.0.1\"")
  }
  
  "compact method invocation syntax" should "be available" in {
    """JavaNew["java.util.Date", {1234}]["toString"[]]""" should evaluateTo ("\"" + new java.util.Date(1234).toString + "\"")
  }
  
}

