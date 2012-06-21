package org.omath.ui.rest

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import scala.io.Source

@RunWith(classOf[JUnitRunner])
class HerokuTest extends FlatSpec with ShouldMatchers {

  "Heroku" should "run a viable omath kernel" in {
	Source.fromURL("http://pure-sky-1860.herokuapp.com/omath/evaluate?syntax=f/@{1,2}").getLines.next should equal(""""List[f[1], f[2]]"""")
  }
  
}

