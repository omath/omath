package org.omath.parser

import org.omath._

object FullFormParser {
	def apply(fullForm: String): Either[Expression, String] = {
	  // FIXME useless hack, no LiteralExpressions, no nested FullFormExpressions
	  fullForm.indexOf("[") match {
	    case -1 => {
	      Left(SymbolExpression(fullForm))
	    }
	    case k => Left(SymbolExpression(fullForm.take(k))(fullForm.drop(k + 1).dropRight(1).split(',').toList.map(apply(_).left.get):_*))
	  }
	}
}

object SyntaxParser {
  def apply(syntax: String): Either[Expression, String] = {
    Syntax2FullFormParser(syntax) match {
      case Left(fullForm) => FullFormParser(fullForm)
      case Right(error) => Right(error)
    }
  }
}

object Syntax2FullFormParser {
  def apply(syntax: String): Either[String, String] = {
    ???
  }
}