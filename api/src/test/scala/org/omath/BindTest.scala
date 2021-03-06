package org.omath

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._

@RunWith(classOf[JUnitRunner])
class BindTest extends FlatSpec with Matchers {

	"bindOption" should "flatten Sequences" in {
	  val a = SymbolExpression("a")
	  val b = SymbolExpression("b")
	  a(b).bind(Map(b -> symbols.Sequence(b, b))) should equal(a(b, b))
	}
  
}