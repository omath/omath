package org.omath.expression

object MockExpressionBuilder extends ExpressionBuilder[Expression] {
    override def createStringExpression(s: String) = MockExpression.StringExpression(s)
    override def createIntegerExpression(s: String) = MockExpression.IntegerExpression(s)
    override def createRealExpression(s: String) = MockExpression.RealExpression(s)
    override def createSymbolExpression(s: String) = MockExpression.SymbolExpression(s)
    override def createFullFormExpression(head: Expression, arguments: Seq[Expression]) = MockExpression.FullFormExpression(head, arguments)
}