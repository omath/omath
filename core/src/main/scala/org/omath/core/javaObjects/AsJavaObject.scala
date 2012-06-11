package org.omath.core.javaObjects

import org.omath.Expression
import org.omath.bootstrap.JavaObjectExpression

object AsJavaObject {
	def apply(expression: Expression) = JavaObjectExpression(expression)
}