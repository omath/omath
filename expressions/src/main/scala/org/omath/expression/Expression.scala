package org.omath.expression

trait Expression
trait RawExpression extends Expression
trait SymbolExpression extends RawExpression {
  def name: String
}
trait LiteralExpression extends RawExpression
trait StringExpression extends LiteralExpression {
  def contents: String
}
trait IntegerExpression extends LiteralExpression
trait RealExpression extends LiteralExpression
trait FullFormExpression extends Expression {
  def head: Expression
  def arguments: Seq[Expression]
}