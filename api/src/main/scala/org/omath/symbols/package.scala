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
  val Blank = SystemSymbolExpression("Blank")
  val List = SystemSymbolExpression("List")
}