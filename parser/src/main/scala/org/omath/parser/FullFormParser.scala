package org.omath.parser

import org.omath._
import scala.util.parsing.combinator._
import scala.util.matching.Regex
import scala.util.parsing.combinator.lexical.Scanners

object FullFormParser extends RegexParsers {
  override val skipWhitespace = false

  def `[` = "["
  def `]` = "]"
  def `,` = ","
  def symbol: Parser[SymbolExpression] = """[a-zA-Z][a-zA-Z0-9\$]*""".r ^^ { case s => SymbolExpression(s) }
  def integer: Parser[IntegerExpression] = """-?[1-9][0-9]*""".r ^^ { case s => IntegerExpression(BigInt(s)) }
  // TODO decimals  
  // TODO strings
  def decimal: Parser[String] = """(\d+(\.\d*)?|\d*\.\d+)""".r ^^ { case s => RealExpression(s) }
  def string: Parser[StringExpression] =
    ("\""+"""([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*"""+"\"").r  ^^ { case s => StringExpression(s) }
  def literal = (symbol | integer | decimal | string)
  def fullForm: Parser[Expression] = {
    (literal ~ (`[` ~ repsep(expression, `,`) ~ `]`).*) ^^ {
      case head ~ argumentLists => {
        argumentLists.foldLeft[Expression](head)({
          case (e, _ ~ arguments ~ _) => e(arguments: _*)
        })
      }
    }
  }
  def expression: Parser[Expression] = fullForm | literal

  def apply(fullForm: String)(implicit symbolizer: String => SymbolExpression): Either[Expression, String] = {
    parseAll(expression, fullForm) match {
      case Success(e, _) => Left(e)
      case Failure(msg, _) => Right(msg)
      case Error(msg, _) => Right(msg)
    }
  }
}

object SyntaxParser {
  def apply(syntax: String)(implicit symbolizer: String => SymbolExpression): Either[Expression, String] = {
    Syntax2FullFormParser(syntax) match {
      case Left(fullForm) => FullFormParser(fullForm)
      case Right(error) => Right(error)
    }
  }
}

object Syntax2FullFormParser {
  def apply(syntax: String): Either[String, String] = {
    try {
      Left(SyntaxParserImpl.parseSyntaxString(syntax).toString)
    } catch {
      case e: Exception => Right(e.getMessage)
    }
  }
}