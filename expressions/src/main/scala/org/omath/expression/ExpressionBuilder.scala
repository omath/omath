package org.omath.expression

trait ExpressionBuilder[E] {
  def createSymbolExpression(s: String): E
  def createIntegerExpression(s: String): E
  def createRealExpression(s: String): E
  def createStringExpression(s: String): E
  def createFullFormExpression(head: E, arguments: Seq[E]): E
}
