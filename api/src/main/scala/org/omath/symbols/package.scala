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
  val Null = SymbolExpression("Null", "System")
  val $Failed = SymbolExpression("$Failed", "System")
  
  val True = SymbolExpression("True", "System")
  val False = SymbolExpression("False", "System")
  
  val String = SymbolExpression("String", "System")
  val Symbol = SymbolExpression("Symbol", "System")
  val Integer = SymbolExpression("Integer", "System")
  val Real = SymbolExpression("Real", "System")
  
  val Set = SymbolExpression("Set", "System")
  val SetDelayed = SymbolExpression("SetDelayed", "System")

  val Rule = SymbolExpression("Rule", "System")
  val RuleDelayed = SymbolExpression("RuleDelayed", "System")

  val Hold = SymbolExpression("Hold", "System")
  val HoldFirst = SymbolExpression("HoldFirst", "System")
  val HoldRest = SymbolExpression("HoldRest", "System")
  val HoldAll = SymbolExpression("HoldAll", "System") 
  val HoldComplete = SymbolExpression("HoldComplete", "System") 
  val HoldPattern = SymbolExpression("HoldPattern", "System") 
  
  val Blank = SymbolExpression("Blank", "System")
  val BlankSequence = SymbolExpression("BlankSequence", "System")
  val BlankNullSequence = SymbolExpression("BlankNullSequence", "System")
  val Pattern = SymbolExpression("Pattern", "System")
  val Alternatives = SymbolExpression("Alternatives", "System")
  val Condition = SymbolExpression("Condition", "System")
  val Repeated = SymbolExpression("Repeated", "System")
  val RepeatedNull = SymbolExpression("RepeatedNull", "System")
  val Optional = SymbolExpression("Optional", "System")
  val PatternSequence = SymbolExpression("PatternSequence", "System")
  val Except = SymbolExpression("Except", "System")
  val Longest = SymbolExpression("Longest", "System")
  val Shortest = SymbolExpression("Shortest", "System")
  val PatternTest = SymbolExpression("PatternTest", "System")
  val Verbatim = SymbolExpression("Verbatim", "System")

  val MatchQ = SymbolExpression("MatchQ", "System")
  val SameQ = SymbolExpression("SameQ", "System")
  
  val $Context = SymbolExpression("$Context", "System")
  val $ContextPath = SymbolExpression("$ContextPath", "System")
}