package org.omath.kernel

import org.omath.SymbolExpression
import org.omath.patterns.ReplacementRule
import org.omath.patterns.ReplacementRuleTable
import org.omath.Context

trait KernelState {
  def attributes(symbol: SymbolExpression): Seq[SymbolExpression]
  def ownValues(symbol: SymbolExpression): ReplacementRuleTable
  def downValues(symbol: SymbolExpression): ReplacementRuleTable
  def subValues(symbol: SymbolExpression): ReplacementRuleTable
  def upValues(symbol: SymbolExpression): ReplacementRuleTable
  def symbols: Map[Context, Seq[SymbolExpression]]

  implicit val attributeFunction = attributes _
  
  def addAttributes(symbol: SymbolExpression, attributes: SymbolExpression*): KernelState
  def addOwnValues(symbol: SymbolExpression, rules: ReplacementRule*): KernelState
  def addDownValues(symbol: SymbolExpression, rules: ReplacementRule*): KernelState
  def addSubValues(symbol: SymbolExpression, rules: ReplacementRule*): KernelState
  def addUpValues(symbol: SymbolExpression, rules: ReplacementRule*): KernelState
}

// TODO move all the following to a .state subpackage?

trait EmptyKernelState extends KernelState {
 
  override def attributes(symbol: SymbolExpression) = Seq.empty
  override def ownValues(symbol: SymbolExpression) = ReplacementRuleTable(Nil)
  override def downValues(symbol: SymbolExpression) = ReplacementRuleTable(Nil)
  override def subValues(symbol: SymbolExpression) = ReplacementRuleTable(Nil)
  override def upValues(symbol: SymbolExpression) = ReplacementRuleTable(Nil)
  override def symbols = Map.empty

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

trait MutableMapKernelState extends MutableKernelState with Serializable {
  def emptyValueMap = scala.collection.mutable.Map[SymbolExpression, ReplacementRuleTable]()

  private val attributesMap = scala.collection.mutable.Map[SymbolExpression, Seq[SymbolExpression]]()
  private val ownValuesMap = emptyValueMap
  private val downValuesMap = emptyValueMap
  private val subValuesMap = emptyValueMap
  private val upValuesMap = emptyValueMap

  private val symbolsMap = scala.collection.mutable.Map[Context, Seq[SymbolExpression]]()

  // scala.collection.mutable.Map#withDefault isn't serializable until M4.
  override def attributes(symbol: SymbolExpression) = attributesMap.getOrElse(symbol, Seq.empty)
  override def ownValues(symbol: SymbolExpression) = ownValuesMap.getOrElse(symbol, ReplacementRuleTable.empty)
  override def downValues(symbol: SymbolExpression) = downValuesMap.getOrElse(symbol, ReplacementRuleTable.empty)
  override def subValues(symbol: SymbolExpression) = subValuesMap.getOrElse(symbol, ReplacementRuleTable.empty)
  override def upValues(symbol: SymbolExpression) = upValuesMap.getOrElse(symbol, ReplacementRuleTable.empty)

  override def symbols = symbolsMap.toMap.withDefaultValue(Seq.empty)
  private def recordSymbol(symbol: SymbolExpression) {
    symbols(symbol.context) match {
      case s if s.contains(symbol) =>
      case s => symbolsMap.put(symbol.context, symbol +: s)
    }
  }

  override def addAttributes(symbol: SymbolExpression, attributes: SymbolExpression*) = {
    recordSymbol(symbol)
    attributesMap.get(symbol) match {
      case None => attributesMap.put(symbol, attributes)
      case Some(oldAttributes) => attributesMap.put(symbol, (oldAttributes ++ attributes).distinct)
    }
    this
  }
  private def addRules(map: scala.collection.mutable.Map[SymbolExpression, ReplacementRuleTable], symbol: SymbolExpression, rules: Seq[ReplacementRule]): this.type = {
    recordSymbol(symbol)
    map.put(symbol, map.getOrElse(symbol, ReplacementRuleTable.empty) ++ rules)
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
