package org.omath

trait KernelState {
  def attributes(symbol: SymbolExpression): Seq[SymbolExpression]
  def ownValues(symbol: SymbolExpression): ReplacementRuleTable
  def downValues(symbol: SymbolExpression): ReplacementRuleTable
}

trait EmptyKernelState extends KernelState {
  override def attributes(symbol: SymbolExpression) = Nil
  override def ownValues(symbol: SymbolExpression) = ReplacementRuleTable()
  override def downValues(symbol: SymbolExpression) = ReplacementRuleTable()
}


trait NoKernelState { kernel: Kernel =>
  override def kernelState = ???
}
