package org.omath.ui.rest

import org.junit.runner.RunWith
import org.omath.bootstrap.JavaObjectExpression
import org.omath.core.EvaluationMatchers
import org.omath.core.TungstenCore
import org.omath.kernel.tungsten.Tungsten
import org.omath.kernel.KernelState
import org.omath.parser.SyntaxParserImplementation
import org.omath.patterns.Pattern.expression2Pattern
import org.omath.symbols.Hold
import org.omath.symbols.List
import org.omath.symbols.MatchQ
import org.omath.symbols.SameQ
import org.omath.symbols.True
import org.omath.Expression
import org.omath.FullFormExpression
import org.omath.StringExpression
import org.omath.SymbolExpression
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

