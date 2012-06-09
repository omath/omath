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
      case Failure(error, None) => System.err.println(error)
      case Failure(error, Some(exception)) => {
        if(error != exception.getMessage) System.err.println(exception)
        System.err.println(exception)
      }
      case Success(org.omath.symbols.Null) => 
      case Success(result) => {
          print("Out[" + lineNumber + "]:= ");
          println(result)        
      }
    }    
    lineNumber = lineNumber + 1
  }

}