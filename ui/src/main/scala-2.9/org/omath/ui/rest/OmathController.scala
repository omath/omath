package org.omath.ui.rest

import org.bowlerframework.controller.Controller
import org.bowlerframework.controller.FunctionNameConventionRoutes
import org.bowlerframework.Request
import net.tqft.toolkit.Logging
import org.omath.core.TungstenCore
import org.omath.core.eval.ScalaEval
import org.omath.core.eval.ScalaEval

class OmathController extends Controller with FunctionNameConventionRoutes with Logging {
  
	def `GET /omath/evaluate`(syntax: String): String = {
	  TungstenCore.evaluateSyntax(syntax).get.toString
	}
}