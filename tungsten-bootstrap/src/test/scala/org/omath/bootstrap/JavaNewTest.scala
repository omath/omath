package org.omath.bootstrap

import org.omath.IntegerExpression
import org.omath.SymbolExpression
import org.omath.kernel._
import org.omath.patterns.ReplacementRule
import org.omath.patterns.ReplacementRuleTable

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Matchers
import org.scalatest.FlatSpec

@RunWith(classOf[JUnitRunner])
class JavaNewTest extends FlatSpec with Matchers {

  "JavaNew" should "successfully instantiate a java.util.Date object" in {
    TungstenBootstrap.evaluateSyntax("""
    		JavaNew["java.util.Date", {}]
        """).get.head should equal(symbols.JavaObject)
  }

}