package org.omath.kernel

import org.omath._
import org.omath.util._
import scala.io.Source

trait ParsingKernel { kernel: Kernel =>
  def parseSyntax(lines: Iterator[String])(implicit symbolizer: String => SymbolExpression): Iterator[Result[Expression]]
  
  def evaluateSyntax(syntax: String): Result[Expression] = {
    evaluateLines(Iterator(syntax.trim))
  }
  def evaluateLines(code: Iterator[String]): Result[Expression] = {
    parseSyntax(code.flatMap(_.split('\n')))(symbolizer).map(_.map(evaluate)).reduce({ (x, y) =>
      if (x.isFailure) System.err.println(x.getFailureMessage)
      y
    })
  }

  def slurp(uri: java.net.URI) {
    evaluateLines(Source.fromURI(uri).getLines)
  }
  def slurp(file: java.io.File) {
    evaluateLines(Source.fromFile(file).getLines)
  }
}