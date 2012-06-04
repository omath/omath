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
  
  val List = SystemSymbolExpression("List")
  
  val String = SystemSymbolExpression("String")
  val Symbol = SystemSymbolExpression("Symbol")
  val Integer = SystemSymbolExpression("Integer")
  val Real = SystemSymbolExpression("Real")

  
  val Blank = SystemSymbolExpression("Blank")
  val BlankSequence = SystemSymbolExpression("BlankSequence")
  val BlankNullSequence = SystemSymbolExpression("BlankNullSequence")
  val Pattern = SystemSymbolExpression("Pattern")
}