package org.omath.symbols

import org.omath.Context
import org.omath.SymbolExpression

class SystemSymbolExpression(override val name: String) extends SymbolExpression {
  override val context = Context.system
}

object Infinity extends SystemSymbolExpression("Infinity")
object OptionsPattern extends SystemSymbolExpression("OptionsPattern")

object Function extends SystemSymbolExpression("Function")
object Slot extends SystemSymbolExpression("Slot")
object SlotSequence extends SystemSymbolExpression("SlotSequence")