package org.omath.kernel.tungsten

import org.omath.kernel.Kernel
import org.omath.kernel.EmptyKernelState
import org.omath.patterns.ReplacementRule
import org.omath.SymbolExpression
import org.omath.patterns.ReplacementRuleTable

import org.omath.symbols.{ Pattern, Blank, List }

trait MockKernelState { kernel: Kernel =>
  def kernelState = new EmptyKernelState { kernelState =>
    override def downValues(symbol: SymbolExpression) = {
      implicit val attributes = kernelState.attributes _
      symbol.name match {
        case "f" => ReplacementRule(SymbolExpression("f")(2), 4)
        case "g" => ReplacementRule(SymbolExpression("g")(Blank()), 6)
        case "h" => ReplacementRule(SymbolExpression("h")(Blank(), Blank()), 8)
        case "k" => ReplacementRule(SymbolExpression("k")(Pattern('x, Blank())), List('x, 'x))
        case _ => ReplacementRuleTable(Nil)
      }
    }
    override def ownValues(symbol: SymbolExpression) = {
      implicit val attributes = kernelState.attributes _
      symbol.name match {
        case "x" => ReplacementRule(SymbolExpression("x"), 2)
        case _ => ReplacementRuleTable(Nil)
      }
    }
  }
}
