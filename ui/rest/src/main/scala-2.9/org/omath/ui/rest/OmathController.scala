package org.omath.ui.rest

import org.bowlerframework.controller.Controller
import org.bowlerframework.controller.FunctionNameConventionRoutes
import org.bowlerframework.Request
import net.tqft.toolkit.Logging
import org.omath.core.TungstenCore

class OmathController extends Controller with FunctionNameConventionRoutes with Logging {
  
	def `GET /omath/evaluate`(syntax: String): String = {
	  TungstenCore.evaluateSyntax(syntax).get.toContextualString(TungstenCore.newEvaluation)
	}
}