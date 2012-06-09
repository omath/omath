package org.omath.bootstrap

import org.omath.kernel.tungsten.Tungsten
import org.omath.kernel.ParsingKernel
import org.omath.SymbolExpression
import org.omath.parser.SyntaxParser

trait TungstenBootstrap extends Tungsten with BootstrapState with ParsingKernel {
  override def parseSyntax(lines: Iterator[String])(implicit symbolizer: String => SymbolExpression) = SyntaxParser(lines)
}
object TungstenBootstrap extends TungstenBootstrap