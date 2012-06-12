package org.omath.bootstrap

import org.omath.kernel.tungsten.Tungsten
import org.omath.SymbolExpression
import org.omath.parser.SyntaxParser
import org.omath.util.Result
import org.omath.Expression
import org.omath.parser.SyntaxParserImplementation

trait TungstenBootstrap extends Tungsten with BootstrapState with SyntaxParserImplementation
object TungstenBootstrap extends TungstenBootstrap