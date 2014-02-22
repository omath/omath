package org.omath.kernel.tungsten

import org.omath.kernel.Kernel
import org.omath.SymbolExpression
import org.omath.parser.SyntaxParser

trait MockSyntaxParser extends SyntaxParser { kernel: Kernel =>
  def parseSyntax(syntax: String)(implicit symbolizer: String => SymbolExpression) = ???
  def parseSyntax(lines: Iterator[String])(implicit symbolizer: String => SymbolExpression) = ???
}
