package org.omath.bootstrap

import org.omath.SymbolExpression

package object symbols {
  def ??? = throw new NoSuchMethodError

  val JavaMethod = SymbolExpression("JavaMethod", "Bootstrap")
  val JavaClass = SymbolExpression("JavaClass", "Bootstrap")
  val JavaObject = SymbolExpression("JavaObject", "Bootstrap")
  val JavaNew = SymbolExpression("JavaNew", "Bootstrap")
}