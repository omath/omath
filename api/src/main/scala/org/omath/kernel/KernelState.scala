package org.omath.kernel

import org.omath.SymbolExpression
import org.omath.patterns.ReplacementRule
import org.omath.patterns.ReplacementRuleTable

trait KernelState {
  def attributes(symbol: SymbolExpression): Set[SymbolExpression]
  def ownValues(symbol: SymbolExpression): ReplacementRuleTable
  def downValues(symbol: SymbolExpression): ReplacementRuleTable
  def subValues(symbol: SymbolExpression): ReplacementRuleTable
  def upValues(symbol: SymbolExpression): ReplacementRuleTable

  def addAttributes(symbol: SymbolExpression, attributes: SymbolExpression*): KernelState
  def addOwnValues(symbol: SymbolExpression, rules: ReplacementRule*): KernelState
  def addDownValues(symbol: SymbolExpression, rules: ReplacementRule*): KernelState
  def addSubValues(symbol: SymbolExpression, rules: ReplacementRule*): KernelState
  def addUpValues(symbol: SymbolExpression, rules: ReplacementRule*): KernelState
}

// TODO move all the following to a .state subpackage?

trait EmptyKernelState extends KernelState {
  override def attributes(symbol: SymbolExpression) = Set.empty
  override def ownValues(symbol: SymbolExpression) = ReplacementRuleTable()
  override def downValues(symbol: SymbolExpression) = ReplacementRuleTable()
  override def subValues(symbol: SymbolExpression) = ReplacementRuleTable()
  override def upValues(symbol: SymbolExpression) = ReplacementRuleTable()

  override def addAttributes(symbol: SymbolExpression, attributes: SymbolExpression*) = ???
  override def addDownValues(symbol: SymbolExpression, rules: ReplacementRule*) = ???
  override def addOwnValues(symbol: SymbolExpression, rules: ReplacementRule*) = ???
  override def addSubValues(symbol: SymbolExpression, rules: ReplacementRule*) = ???
  override def addUpValues(symbol: SymbolExpression, rules: ReplacementRule*) = ???
}

trait EmptyState { kernel: Kernel =>
  override def kernelState = new EmptyKernelState {}
}

trait MutableKernelState extends KernelState {
  override def addAttributes(symbol: SymbolExpression, attributes: SymbolExpression*): this.type
  override def addOwnValues(symbol: SymbolExpression, rules: ReplacementRule*): this.type
  override def addDownValues(symbol: SymbolExpression, rules: ReplacementRule*): this.type
  override def addSubValues(symbol: SymbolExpression, rules: ReplacementRule*): this.type
  override def addUpValues(symbol: SymbolExpression, rules: ReplacementRule*): this.type
}

trait MutableMapKernelState extends MutableKernelState {
  def emptyValueMap = scala.collection.mutable.Map[SymbolExpression, ReplacementRuleTable]().withDefaultValue(ReplacementRuleTable())

  private val attributesMap = scala.collection.mutable.Map[SymbolExpression, Set[SymbolExpression]]().withDefaultValue(Set())
  private val ownValuesMap = emptyValueMap
  private val downValuesMap = emptyValueMap
  private val subValuesMap = emptyValueMap
  private val upValuesMap = emptyValueMap

  override def attributes(symbol: SymbolExpression) = attributesMap(symbol)
  override def ownValues(symbol: SymbolExpression) = ownValuesMap(symbol)
  override def downValues(symbol: SymbolExpression) = downValuesMap(symbol)
  override def subValues(symbol: SymbolExpression) = subValuesMap(symbol)
  override def upValues(symbol: SymbolExpression) = upValuesMap(symbol)

  override def addAttributes(symbol: SymbolExpression, attributes: SymbolExpression*) = {
    attributesMap.get(symbol) match {
      case None => attributesMap.put(symbol, attributes.toSet)
      case Some(oldAttributes) => attributesMap.put(symbol, oldAttributes ++ attributes)
    }
    this
  }
  private def addRules(map: scala.collection.mutable.Map[SymbolExpression, ReplacementRuleTable], symbol: SymbolExpression, rules: Seq[ReplacementRule]): this.type = {
    // FIXME rule specificity, etc.
    map.put(symbol, ReplacementRuleTable((map(symbol).table ++ rules):_*))
    this
  }
  
  override def addOwnValues(symbol: SymbolExpression, rules: ReplacementRule*) = {
    addRules(ownValuesMap, symbol, rules)
  }
  override def addDownValues(symbol: SymbolExpression, rules: ReplacementRule*) = {
    addRules(downValuesMap, symbol, rules)
  }
  override def addSubValues(symbol: SymbolExpression, rules: ReplacementRule*) = {
    addRules(subValuesMap, symbol, rules)
  }
  override def addUpValues(symbol: SymbolExpression, rules: ReplacementRule*) = {
    addRules(upValuesMap, symbol, rules)
  }
}

class MutableMapKernelStateWrapper(oldKernelState: KernelState) extends MutableMapKernelState {
  // TODO copy everything into the new state
}

trait MutableState { kernel: Kernel =>
  override def kernelState = new MutableMapKernelState {}
}