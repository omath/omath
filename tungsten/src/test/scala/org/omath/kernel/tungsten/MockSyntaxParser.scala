package org.omath.kernel.tungsten

import org.omath.kernel.Kernel
import org.omath.SymbolExpression
import org.omath.parser.SyntaxParser
import org.omath.expression.ExpressionBuilder
import org.omath.Expression

trait MockSyntaxParser extends SyntaxParser { kernel: Kernel =>
  override def parseSyntax[E](syntax: String)(implicit builder: ExpressionBuilder[E]) = ???
  override def parseSyntax[E](lines: Iterator[String])(implicit builder: ExpressionBuilder[E]) = ???
}
