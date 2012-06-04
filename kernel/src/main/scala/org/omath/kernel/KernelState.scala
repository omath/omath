package org.omath.kernel

import org.omath._
import org.omath.patterns._

trait EmptyKernelState extends KernelState {
  override def attributes(symbol: SymbolExpression) = Nil
  override def ownValues(symbol: SymbolExpression) = ReplacementRuleTable()
  override def downValues(symbol: SymbolExpression) = ReplacementRuleTable()
}

trait EmptyState { kernel: Kernel =>
  override def kernelState = new EmptyKernelState {}
}

trait MutableKernelState extends KernelState {
  private val attributesMap = scala.collection.mutable.Map[SymbolExpression, Seq[SymbolExpression]]().withDefaultValue(Seq())
  private val ownValuesMap = scala.collection.mutable.Map[SymbolExpression, ReplacementRuleTable]().withDefaultValue(ReplacementRuleTable())
  private val downValuesMap = scala.collection.mutable.Map[SymbolExpression, ReplacementRuleTable]().withDefaultValue(ReplacementRuleTable())
  override def attributes(symbol: SymbolExpression) = attributesMap(symbol)
  override def ownValues(symbol: SymbolExpression) = ownValuesMap(symbol)
  override def downValues(symbol: SymbolExpression) = downValuesMap(symbol)
}

trait MutableState { kernel: Kernel =>
  override def kernelState = new MutableKernelState {}
}