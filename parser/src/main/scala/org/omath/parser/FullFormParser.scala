package org.omath.parser

import org.omath._
import scala.util.parsing.combinator._
import scala.util.matching.Regex
import scala.util.parsing.combinator.lexical.Scanners

object FullFormParser extends RegexParsers {
  override val skipWhitespace = false

  private def `[` = "["
  private def `]` = "]"
  private def `,` = ","
  private def whitespace: Parser[String] = " "
  private def symbol(implicit symbolizer: String => SymbolExpression): Parser[SymbolExpression] = """[a-zA-Z\$][a-zA-Z0-9\$]*""".r ^^ { case s => symbolizer(s) }
  private def integer: Parser[IntegerExpression] = """-?[1-9][0-9]*""".r ^^ { case s => IntegerExpression(BigInt(s)) }
  // from https://github.com/scala/scala/blob/v2.9.2/src/library/scala/util/parsing/combinator/JavaTokenParsers.scala
  private def decimal: Parser[RealExpression] = """(\d+(\.\d*)?|\d*\.\d+)""".r ^^ { case s => RealExpression(BigDecimal(s)) }
  private def string: Parser[StringExpression] =
    ("\"" + """([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*""" + "\"").r ^^ { case s => StringExpression(s.stripPrefix("\"").stripSuffix("\"")) }
  private def literal(implicit symbolizer: String => SymbolExpression): Parser[RawExpression] = (symbol | integer | decimal | string)
  private def fullForm(implicit symbolizer: String => SymbolExpression): Parser[Expression] = {
    (literal ~ (`[` ~ repsep(expression, `,` ~ whitespace.*) ~ `]`).*) ^^ {
      case head ~ argumentLists => {
        argumentLists.foldLeft[Expression](head)({
          case (e, _ ~ arguments ~ _) => e(arguments: _*)
        })
      }
    }
  }
  private def empty = "" ^^ { case _ => symbols.Null }
  private def expression(implicit symbolizer: String => SymbolExpression): Parser[Expression] = whitespace.* ~> (fullForm | literal | empty) <~ whitespace.*

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
  @scala.annotation.tailrec
  private def stripComments(syntax: String): String = {
    syntax.indexOf("(*") match {
      case -1 => syntax
      case k => {
        syntax.indexOf("*)") match {
          case j if j > k => {
            stripComments(syntax.take(k) + syntax.drop(j + 2))
          }
          case -1 => {
            throw new ParseException("Comment started with '(*' but did not close with a matching '*)'")
          }
          case _ => {
            throw new ParseException("Comment closes with '*)' before opening with a '(*'")
          }
        }
      }
    }
  }

  def apply(syntax: String): Either[String, String] = {
    try {
      stripComments(syntax).trim match {
        case "" => Left("")
        case stripped => Left(SyntaxParserImpl.parseSyntaxString(stripped).toString)
      }
    } catch {
      //      case e: Exception => Right(e.toString)
      case e: Exception => throw e
    }
  }
}