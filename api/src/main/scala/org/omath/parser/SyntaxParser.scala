package org.omath.parser

import org.omath._
import org.omath.util._

trait SyntaxParser {
  def parseSyntax(syntax: String)(implicit symbolizer: String => SymbolExpression): Result[Expression]
  def parseSyntax(lines: Iterator[String])(implicit symbolizer: String => SymbolExpression): Iterator[Result[Expression]]
}
