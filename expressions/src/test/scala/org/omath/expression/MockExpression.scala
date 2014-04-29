package org.omath.expression

object MockExpression {
  case class StringExpression(contents: String) extends org.omath.expression.StringExpression {
    override def toString = "\"" + contents + "\""
  }
  case class RealExpression(value: String) extends org.omath.expression.RealExpression {
    override def toString = value
  }
  case class IntegerExpression(value: String) extends org.omath.expression.IntegerExpression {
    override def toString = value
  }
  case class SymbolExpression(name: String) extends org.omath.expression.SymbolExpression {
    override def toString = name
  }
  case class FullFormExpression(head: Expression, arguments: Seq[Expression]) extends org.omath.expression.FullFormExpression {
    override def toString = arguments.mkString(head + "[", "," , "]")
  }
}