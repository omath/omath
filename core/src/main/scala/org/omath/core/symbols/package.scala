package org.omath.core

import org.omath.SymbolExpression

package object symbols {
  // FIXME shouldn't be in System
  val ScalaObject = SymbolExpression("ScalaObject", "System")
  
  val $Context = SymbolExpression("$Context", "System")
  val $ContextPath = SymbolExpression("$ContextPath", "System")
}