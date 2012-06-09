package org.omath.ui.repl

import org.omath.util._
import org.omath.SymbolExpression
import org.omath.core.TungstenCore
import org.omath.parser.SyntaxParser
import java.io.BufferedReader
import java.io.InputStreamReader

object omath extends App {

  val in = new BufferedReader(new InputStreamReader(System.in))

  var lineNumber = 1;

  while (true) {
    print("In[" + lineNumber + "] := ");
    TungstenCore.evaluateSyntax(in.readLine()) match {
      case Failure(error) => System.err.println(error)
      case Success(org.omath.symbols.Null) => 
      case Success(result) => {
          print("Out[" + lineNumber + "]:= ");
          println(result)        
      }
    }    
    lineNumber = lineNumber + 1
  }

}