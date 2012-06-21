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
import jline.ConsoleReader

object in {
  
  // jLine doesn't work properly when run in Eclipse.
  private val reader = if (System.console != null) {
    Left(new ConsoleReader())
  } else {
    Right(new BufferedReader(new InputStreamReader(System.in)))
  }
  
  def readLine(prompt:String): String = {
    reader match {
      case Left(reader) => reader.readLine(prompt)
      case Right(reader) => {
        System.out.print(prompt)
        reader.readLine
      }
    }
  }
}

object omath extends App {

  Get("scratch.m")(TungstenCore)

  var lineNumber = 1;

  var running = true
  Exit.registerExitListener({ () => running = false })

  while (running) {
    TungstenCore.evaluateSyntax(in.readLine("In[" + lineNumber + "] := ")) match {
      case Failure(error, None) => System.err.println(error)
      case Failure(error, Some(exception)) => {
        if (error != exception.getMessage) System.err.println(error)
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