package org.omath.ui.repl

import org.omath.util._
import org.omath.SymbolExpression
import org.omath.core.TungstenCore
import org.omath.parser.SyntaxParser
import java.io.BufferedReader
import java.io.InputStreamReader
import org.omath.core.io.$Path
import org.omath.core.io.Get
import org.omath.core.kernel.Exit

object omath extends App {

  Get("scratch.m")(TungstenCore)
  
  val in = new BufferedReader(new InputStreamReader(System.in))

  var lineNumber = 1;

  var running = true
  Exit.registerExitListener({ () => running = false })
  
  while (running) {
    print("In[" + lineNumber + "] := ");
    TungstenCore.evaluateSyntax(in.readLine()) match {
      case Failure(error, None) => System.err.println(error)
      case Failure(error, Some(exception)) => {
        if(error != exception.getMessage) System.err.println(error)
        System.err.println(exception)
        throw exception
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