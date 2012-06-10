package org.omath.core

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.omath.util._
import org.omath.StringExpression
import org.omath.SymbolExpression

@RunWith(classOf[JUnitRunner])
class SetTest extends FlatSpec with ShouldMatchers with EvaluationMatchers {

  "SetDelayed" should "hold its right hand side" in {
    """f[x_]:={x,x}
       x=2
       f[3]""" should evaluateTo("List[3, 3]")
  }

  "Set" should "evaluate its right hand side immediately" in {
    """x=2
       g[x_]={x,x,x}
       g[3]""" should evaluateTo("List[2, 2, 2]")
  }  
}

