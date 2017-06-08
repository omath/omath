package org.omath.core.attributes

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Matchers
import org.scalatest.FlatSpec
import org.omath.util._
import org.omath.StringExpression
import org.omath.SymbolExpression
import org.omath.core.EvaluationMatchers
import org.omath.core.TungstenCore

@RunWith(classOf[JUnitRunner])
class AttributesTest extends FlatSpec with Matchers with EvaluationMatchers {

  
  "SetDelayed" should "be HoldAll" in {
    "Attributes[SetDelayed]" should evaluateTo("List[HoldAll]")
  }
}

