package org.omath.core.eval

import net.tqft.toolkit.Logging
import scala.tools.nsc.Settings
import java.io.PipedInputStream
import java.io.BufferedReader
import scala.collection.mutable.ListBuffer
import scala.tools.nsc.interpreter.IMain
import java.io.InputStreamReader
import java.io.PrintWriter
import java.io.PipedOutputStream

trait Eval {

  protected val settings = {
    val s = new Settings

    // see https://gist.github.com/404272
    s.embeddedDefaults[Eval]

    // What a hack! Really I should understand what goes wrong in each situation, and detect that directly.
    if (!classOf[Eval].getClassLoader.getClass.getName.startsWith("sbt")) {
      s.classpath.value = System.getProperty("java.class.path")
      s.usejavacp.value = true
    }
    s
  }

  private val pis = new PipedInputStream
  private val reader = new BufferedReader(new InputStreamReader(pis))
  private def newConsoleLines = {
    val output = new ListBuffer[String]
    while (reader.ready) {
      output += reader.readLine()
    }
    output.mkString("\n")
  }

  private lazy val interpreter = new IMain(settings, new PrintWriter(new PipedOutputStream(pis)))
  private lazy val _history = new ListBuffer[(String, Option[(String, Any)], String)]

  def history = _history.toList

  def outputNames = _history collect { case (_, Some((name, _)), _) => name }
  def lastOutputName = outputNames.lastOption

  private def extractOutput = interpreter.mostRecentVar match {
    case name if Some(name) == lastOutputName || name == "" => /* nothing new */ None
    case name => Some((name, interpreter.valueOfTerm(name).get))
  }

  def apply(command: String): Option[Any] = eval(command)

  def evalWithNameAndOutput(command: String) = {
    interpreter.interpret(command)
    val output = extractOutput
    val consoleLines = newConsoleLines
    _history += ((command, output, consoleLines))
    (output, consoleLines)
  }
  def eval(command: String): Option[Any] = evalWithNameAndOutput(command)._1 map { _._2 }

}

object Eval extends Eval

object ScalaEval extends Logging {
  try {
	  init
  } catch {
    case e => e.printStackTrace
  }
  
  lazy val init = {
    info("Initializing the scala compiler...")

    Eval("import org.omath._")
    Eval("import org.omath.kernel._")
    Eval("import org.omath.parser._")
    Eval("import org.omath.patterns._")

    require(Eval("org.omath.SymbolExpression").nonEmpty)
    info("... scala compiler ready!")
  }

  def apply(code: String): Any = {
    init
    
    info("evaluating '" + code + "' using the Scala REPL")
    val (result, output) = Eval.evalWithNameAndOutput(code)
    result match {
      case Some((text, value)) => {
        info("Scala REPL output: " + text)
        info(value)
        value
      }
      case None => {
        System.err.println("ScalaEval failed, with output:")
        System.err.println(output)
        org.omath.symbols.$Failed
      }
    }
  }
}