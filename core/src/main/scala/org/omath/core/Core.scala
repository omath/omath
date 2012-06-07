package org.omath.core

import org.omath.Expression
import org.omath.SymbolExpression
import org.omath.parser.SyntaxParser
import org.omath.kernel.Kernel
import scala.io.Source
import scala.collection.JavaConversions

sealed trait Result[T]
case class Success[T](value: T) extends Result[T]
case class Failure[T](message: String) extends Result[T]

trait Core extends Kernel {
  def evaluate(syntax: String): Result[Expression] = {
    SyntaxParser(syntax)(symbolizer) match {
      case Left(expression) => Success(evaluate(expression))
      case Right(message) => Failure(message)
    }
  }
  def slurp(code: Source) {
    for (line <- code.getLines) {
      evaluate(line) match {
        case Success(_) =>
        case Failure(message) => {
          System.err.println("Parse error while processing: " + line)
          System.err.println(message)
        }
      }
    }
  }

  {
    import JavaConversions._
    val resources: Iterator[java.net.URL] = this.getClass.getClassLoader.getResources("")
    val coreClasses = resources.map(_.toString).filter(_.contains("/core/")).next
    val `core.m` = coreClasses.take(coreClasses.indexOf("/core/") + 6) + "src/main/omath/core.m"
    slurp(Source.fromURI(new java.net.URI(`core.m`)))
  }

  // FIXME look at $Context and $ContextPath
  private def symbolizer = { s: String => SymbolExpression(s, "System") }

}