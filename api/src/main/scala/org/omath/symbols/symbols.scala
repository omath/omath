package org.omath.symbols

import org.omath.Context
import org.omath.SymbolExpression
import org.omath.OneIdentity

class SystemSymbolExpression(override val name: String) extends SymbolExpression {
  override val context = Context.system
}

object Infinity extends SystemSymbolExpression("Infinity")
object OptionsPattern extends SystemSymbolExpression("OptionsPattern")

object Function extends SystemSymbolExpression("Function")
object Slot extends SystemSymbolExpression("Slot")
object SlotSequence extends SystemSymbolExpression("SlotSequence")

object Power extends SystemSymbolExpression("Power")
object Plus extends SystemSymbolExpression("Plus") with OneIdentity
object Times extends SystemSymbolExpression("Times") with OneIdentity