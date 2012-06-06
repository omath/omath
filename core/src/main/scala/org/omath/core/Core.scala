package org.omath.core

import org.omath.Expression
import org.omath.SymbolExpression
import org.omath.parser.SyntaxParser
import org.omath.kernel.Kernel
import scala.io.Source

sealed trait Result[T]
case class Success[T](value: T) extends Result[T]
case class Failure[T](message: String) extends Result[T]

trait Core extends Kernel {
	def evaluate(syntax: String): Result[Expression] = {
	  SyntaxParser(syntax)(symbolizer) match {
	    case Left(expression)  => Success(evaluate(expression))
	    case Right(message) => Failure(message)
	  }
	}
	def slurp(code: Source) {
	  for(line <- code.getLines) {
	    evaluate(line) match {
	      case Success(_) => 
	      case Failure(message) => System.err.println(message)
	    }
	  }
	}
	
	slurp(Source.fromFile("../core/src/main/omath/core.m"))
	
	// FIXME look at $Context and $ContextPath
	private def symbolizer = { s: String => SymbolExpression(s, "System") }
	
}