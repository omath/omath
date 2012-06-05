package org.omath

import patterns.ReplacementRuleTable

trait KernelState {
  def attributes(symbol: SymbolExpression): Seq[SymbolExpression]
  def ownValues(symbol: SymbolExpression): ReplacementRuleTable
  def downValues(symbol: SymbolExpression): ReplacementRuleTable
  def subValues(symbol: SymbolExpression): ReplacementRuleTable
  def upValues(symbol: SymbolExpression): ReplacementRuleTable
}