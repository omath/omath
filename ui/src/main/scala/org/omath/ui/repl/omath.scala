package org.omath.ui.repl

import org.omath.SymbolExpression
import org.omath.core.TungstenCore
import org.omath.parser.SyntaxParser
import java.io.BufferedReader
import java.io.InputStreamReader

object omath extends App {

  // FIXME
  implicit val symbolizer = { s: String => SymbolExpression(s, "System") }

  val in = new BufferedReader(new InputStreamReader(System.in))

  var lineNumber = 1;

  while (true) {
    print("In[" + lineNumber + "] := ");
    SyntaxParser(in.readLine()) match {
      case Right(error) => System.err.println(error)
      case Left(parsed) => {
        val result = TungstenCore.evaluate(parsed)
        print("Out[" + lineNumber + "]:= ");
        println(result)
      }
    }
    lineNumber = lineNumber + 1
  }

}