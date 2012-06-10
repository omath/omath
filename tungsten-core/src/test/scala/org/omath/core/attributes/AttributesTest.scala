package org.omath.core.attributes

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.util._
import org.omath.StringExpression
import org.omath.SymbolExpression
import org.omath.core.EvaluationMatchers

@RunWith(classOf[JUnitRunner])
class AttributesTest extends FlatSpec with ShouldMatchers with EvaluationMatchers {

  "SetDelayed" should "be HoldAll" in {
    "Attributes[SetDelayed]" should evaluateTo ("List[HoldAll]")
  }
}
