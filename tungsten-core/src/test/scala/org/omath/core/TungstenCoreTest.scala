package org.omath.core

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.util._
import org.omath.StringExpression
import org.omath.SymbolExpression

@RunWith(classOf[JUnitRunner])
class TungstenCoreTest extends FlatSpec with ShouldMatchers {

  "TungstenCore" should "correctly evaluate $Version, set in core.m" in {
    TungstenCore.evaluateSyntax("$Version") should equal(Success(StringExpression("0.0.1")))
  }
}

