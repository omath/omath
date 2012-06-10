package org.omath.parser

import org.omath._
import org.omath.util._
import scala.util.parsing.combinator._
import scala.util.matching.Regex
import scala.util.parsing.combinator.lexical.Scanners

import org.apfloat.Apint
import org.apfloat.Apfloat


object FullFormParser extends RegexParsers {
  override val skipWhitespace = false

  private def `[` = "["
  private def `]` = "]"
  private def `,` = ","
  private def whitespace: Parser[String] = " "
  private def symbol(implicit symbolizer: String => SymbolExpression): Parser[SymbolExpression] = """[a-zA-Z\$][a-zA-Z0-9\$]*""".r ^^ { case s => symbolizer(s) }
  private def integer: Parser[IntegerExpression] = """-?[0-9]+""".r ^^ { case s => IntegerExpression(new Apint(s)) }
  private def decimal: Parser[RealExpression] = """-?([0-9]*\.[0-9]+|[0-9]+\.[0-9]*)""".r ^^ { case s => RealExpression(new Apfloat(s)) }
  // from https://github.com/scala/scala/blob/v2.9.2/src/library/scala/util/parsing/combinator/JavaTokenParsers.scala
  private def string: Parser[StringExpression] =
    ("\"" + """([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*""" + "\"").r ^^ { case s => StringExpression(s.stripPrefix("\"").stripSuffix("\"")) }
  private def literal(implicit symbolizer: String => SymbolExpression): Parser[RawExpression] = (symbol | decimal | integer | string)
  private def fullForm(implicit symbolizer: String => SymbolExpression): Parser[Expression] = {
    (literal ~ (`[` ~ repsep(expression, `,` ~ whitespace.*) ~ `]`).*) ^^ {
      case head ~ argumentLists => {
        argumentLists.foldLeft[Expression](head)({
          case (e, _ ~ arguments ~ _) => e(arguments: _*)
        })
      }
    }
  }
  private def expression(implicit symbolizer: String => SymbolExpression): Parser[Expression] = fullForm | literal
    private def empty = "" ^^ { case _ => symbols.Null }

  private def entireExpression(implicit symbolizer: String => SymbolExpression): Parser[Expression] = whitespace.* ~> (expression | empty) <~ whitespace.*
  
  def apply(fullForm: String)(implicit symbolizer: String => SymbolExpression): Result[Expression] = {
    parseAll(entireExpression, fullForm) match {
      case Success(e, _) => org.omath.util.Success(e)
      case Failure(msg, _) => org.omath.util.Failure(msg)
      case Error(msg, _) => org.omath.util.Failure(msg)
    }
  }
}

object SyntaxParser {
  def apply(syntax: String)(implicit symbolizer: String => SymbolExpression): Result[Expression] = {
    Syntax2FullFormParser(syntax).flatMap(FullFormParser(_))
  }
  def apply(lines: Iterator[String])(implicit symbolizer: String => SymbolExpression): Iterator[Result[Expression]] = {
    new Iterator[Result[Expression]] {
      val cache = scala.collection.mutable.ListBuffer[String]()
      def parseCache = apply(cache.mkString(" "))
      var nextOption: Option[Result[Expression]] = produceNextOption
      @scala.annotation.tailrec
      private def produceNextOption: Option[Result[Expression]] = {
        def parseAndClearCache = {
          val result = parseCache
          cache.clear
          result
        }
        if (lines.hasNext) {
          lines.next.trim match {
            case "" => {
              // if we reach an empty line, output a Failure
              Some(parseAndClearCache)
            }
            case nextLine => {
              cache += nextLine
              parseCache match {
                case p @ Success(_) => {
                  // we managed to parse a chunk!
                  cache.clear
                  Some(p)
                }
                case _ => {
                  // keep trying 
                  produceNextOption
                }
              }
            }
          }
        } else {
          // ran out of lines
          if (cache.nonEmpty) {
            // if there's something in the cache, output the last Failure
            Some(parseAndClearCache)
          } else {
            // otherwise say we're done
            None
          }
        }
      }
      override def hasNext = nextOption.nonEmpty
      override def next = {
        val result = nextOption.get
        nextOption = produceNextOption
        result
      }
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

  def apply(syntax: String): Result[String] = {
    try {
      stripComments(syntax).trim match {
        case "" => Success("")
        case stripped => Success(SyntaxParserImpl.parseSyntaxString(stripped).toString)
      }
    } catch {
      case e: Exception => {
        Failure(e.toString, Some(e))
      }
    }
  }
}