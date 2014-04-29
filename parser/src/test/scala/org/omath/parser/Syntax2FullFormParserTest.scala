package org.omath.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import scala.util.Success

@RunWith(classOf[JUnitRunner])
class Syntax2FullFormParserTest extends FlatSpec with Matchers {

  "Syntax2FullFormParser" should "parse syntax strings" in {
    Syntax2FullFormParser("a + b") should equal(Success("Plus[a, b]"))
  }
}