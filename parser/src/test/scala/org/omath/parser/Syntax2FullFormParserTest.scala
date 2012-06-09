package org.omath.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.util._

@RunWith(classOf[JUnitRunner])
class Syntax2FullFormParserTest extends FlatSpec with ShouldMatchers {

  "Syntax2FullFormParser" should "parse syntax strings" in {
    Syntax2FullFormParser("a + b") should equal(Success("Plus[a, b]"))
  }
}