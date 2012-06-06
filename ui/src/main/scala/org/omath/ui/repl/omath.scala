package org.omath.ui.repl

import org.omath.SymbolExpression
import org.omath.bootstrap.TungstenBootstrap
import org.omath.parser.SyntaxParser

object omath extends App {

  implicit val symbolizer = { s: String => SymbolExpression(s) }
  
  while(true) {
    println(TungstenBootstrap.evaluate(SyntaxParser(readLine).left.get))
  }
}