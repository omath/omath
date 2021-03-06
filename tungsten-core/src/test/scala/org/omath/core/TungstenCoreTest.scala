package org.omath.core

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Matchers
import org.scalatest.FlatSpec
import org.omath.util._
import org.omath.StringExpression
import org.omath.SymbolExpression

@RunWith(classOf[JUnitRunner])
class TungstenCoreTest extends FlatSpec with Matchers with EvaluationMatchers {

  "$Version" should "correctly evaluate, after being set in Core.m" in {
    "$Version" should evaluateTo ("\"0.0.1\"")
  }
  
  "compact method invocation syntax" should "be available" in {
    """JavaNew["java.util.Date", {1234}]["toString"[]]""" should evaluateTo ("\"" + new java.util.Date(1234).toString + "\"")
  }
  
}

