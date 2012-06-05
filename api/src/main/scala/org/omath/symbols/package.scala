package org.omath

package object symbols {
  val Sequence = new SymbolExpression {
    override val name = "Sequence"
    override val context = Context.system
    override def apply(arguments: Expression*) = {
      if (arguments.size == 1) {
        arguments.head
      } else {
        super.apply(arguments:_*)
      }
    }
  }
  
  val List = SymbolExpression("List", "System")
  
  val String = SymbolExpression("String", "System")
  val Symbol = SymbolExpression("Symbol", "System")
  val Integer = SymbolExpression("Integer", "System")
  val Real = SymbolExpression("Real", "System")

  
  val Blank = SymbolExpression("Blank", "System")
  val BlankSequence = SymbolExpression("BlankSequence", "System")
  val BlankNullSequence = SymbolExpression("BlankNullSequence", "System")
  val Pattern = SymbolExpression("Pattern", "System")
}