package org.omath.parser

import org.omath.expression._
import scala.util.parsing.combinator._
import scala.util.matching.Regex
import scala.util.parsing.combinator.lexical.Scanners
import scala.util.{Try, Success, Failure}


object FullFormParser extends RegexParsers {
  override val skipWhitespace = false

  private def `[` = "["
  private def `]` = "]"
  private def `,` = ","
  private def whitespace: Parser[String] = " "
  private def symbol[E](implicit builder: ExpressionBuilder[E]): Parser[E] = """[a-zA-Z\$][a-zA-Z0-9\$]*""".r ^^ { case s => builder.createSymbolExpression(s) }
  private def integer[E](implicit builder: ExpressionBuilder[E]): Parser[E] = """-?[0-9]+""".r ^^ { case s => builder.createIntegerExpression(s) }
  private def decimal[E](implicit builder: ExpressionBuilder[E]): Parser[E] = """-?([0-9]*\.[0-9]+|[0-9]+\.[0-9]*)""".r ^^ { case s => builder.createRealExpression(s) }
  // from https://github.com/scala/scala/blob/v2.9.2/src/library/scala/util/parsing/combinator/JavaTokenParsers.scala
  private def string[E](implicit builder: ExpressionBuilder[E]): Parser[E] =
    ("\"" + """([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*""" + "\"").r ^^ { case s => builder.createStringExpression(s.stripPrefix("\"").stripSuffix("\"")) }
  private def literal[E](implicit builder: ExpressionBuilder[E]): Parser[E] = (symbol | decimal | integer | string)
  private def fullForm[E](implicit builder: ExpressionBuilder[E]): Parser[E] = {
    (literal ~ (`[` ~ repsep(expression, `,` ~ whitespace.*) ~ `]`).*) ^^ {
      case head ~ argumentLists => {
        argumentLists.foldLeft[E](head)({
          case (e, _ ~ arguments ~ _) => builder.createFullFormExpression(e, arguments)
        }) 
      }
    }
  }
  private def expression[E](implicit builder: ExpressionBuilder[E]): Parser[E] = fullForm | literal
  private def empty[E](implicit builder: ExpressionBuilder[E]) = "" ^^ { case _ => builder.createSymbolExpression("Null") }

  private def entireExpression[E](implicit builder: ExpressionBuilder[E]): Parser[E] = whitespace.* ~> (expression | empty) <~ whitespace.*
  
  def apply[E](fullForm: String)(implicit builder: ExpressionBuilder[E]): Try[E] = {
    parseAll(entireExpression, fullForm) match {
      case Success(e, _) => scala.util.Success(e)
      case Failure(msg, _) => scala.util.Failure(new ParseException(msg))
      case Error(msg, _) => scala.util.Failure(new ParseException(msg))
    }
  }
}



