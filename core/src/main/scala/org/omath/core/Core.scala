package org.omath.core

import org.omath.Expression
import org.omath.SymbolExpression
import org.omath.parser.SyntaxParser
import org.omath.kernel.Kernel
import org.omath.util._
import scala.io.Source
import scala.collection.JavaConversions

trait Core extends Kernel {
  def evaluate(syntax: String): Result[Expression] = {
    SyntaxParser(syntax)(symbolizer).map(evaluate)
  }
  def slurp(code: Source) {
    SyntaxParser(code.getLines)(symbolizer).map({
      case Success(e) => {
        println("evaluating: " + e)
        evaluate(e)
      }
      case Failure(message) => {
        System.err.println(message)
      }
    }).size // need to force the iterator!
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