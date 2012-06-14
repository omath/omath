package org.omath.kernel

import org.omath.Expression
import org.omath.patterns.ReplacementRuleTable
import org.omath.SymbolExpression
import org.omath.util._
import scala.io.Source
import org.omath.parser.SyntaxParser

trait Kernel { kernel: SyntaxParser =>
  def kernelState: KernelState
  def newEvaluation: Evaluation
  def evaluate(expression: Expression): Expression = newEvaluation.evaluate(expression)

  // this will be overriden later, but is needing for unit testing
  protected def symbolizer = { s: String => SymbolExpression(s, "System") }

  def evaluateSyntax(syntax: String): Result[Expression] = {
    evaluateSyntax(Iterator(syntax.trim))
  }
  def evaluateSyntax(code: Iterator[String]): Result[Expression] = {
    if (code.hasNext) {
      parseSyntax(code.flatMap(_.split('\n')))(symbolizer).map(_.map(evaluate)).reduce({ (x, y) =>
        x match {
          case x: Failure[_] => {
            System.err.println(x)
          }
          case _: Success[_] =>
        }
        y
      })
    } else {
      Success(org.omath.symbols.Null)
    }
  }

  def slurp(uri: java.net.URI) {
    evaluateSyntax(Source.fromURI(uri).getLines)
  }
  def slurp(file: java.io.File) {
    evaluateSyntax(Source.fromFile(file).getLines)
  }

}