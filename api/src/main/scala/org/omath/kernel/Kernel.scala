package org.omath.kernel

import org.omath._
import org.omath.patterns.ReplacementRuleTable
import scala.io.Source
import org.omath.parser.SyntaxParser
import org.omath.expression.ExpressionBuilder
import org.apfloat.Apint
import org.apfloat.Apfloat
import scala.util.{Try, Success, Failure}

trait Kernel { kernel: SyntaxParser =>
  def kernelState: KernelState
  def newEvaluation: Evaluation
  def evaluate(expression: Expression): Expression = newEvaluation.evaluate(expression)

  // this will be overriden later, but is needing for unit testing
  protected def symbolizer = { s: String => SymbolExpression(s, "System") }
  protected def builder = new ExpressionBuilder[Expression] {
    override def createStringExpression(s: String) = StringExpression(s)
    override def createIntegerExpression(s: String) = IntegerExpression(new Apint(s))
    override def createRealExpression(s: String) = RealExpression(new Apfloat(s))
    override def createSymbolExpression(s: String) = SymbolExpression(s, "System")
    override def createFullFormExpression(head: Expression, arguments: Seq[Expression]) = head(arguments:_*)
  }

  def evaluateSyntax(syntax: String): Try[Expression] = {
    evaluateSyntax(Iterator(syntax.trim))
  }
  def evaluateSyntax(code: Iterator[String]): Try[Expression] = {
    if (code.hasNext) {
      parseSyntax(code.flatMap(_.split('\n')))(builder).map(_.map(evaluate)).reduce({ (x, y) =>
        x match {
          case Failure(e) => {
            System.err.println(e)
          }
          case Success(_) =>
        }
        y
      })
    } else {
      Success(org.omath.symbols.Null)
    }
  }

  def slurp(url: java.net.URL) {
    evaluateSyntax(Source.fromURL(url).getLines)
  }
  def slurp(file: java.io.File) {
    evaluateSyntax(Source.fromFile(file).getLines)
  }

}