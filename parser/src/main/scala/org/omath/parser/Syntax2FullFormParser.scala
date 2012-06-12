package org.omath.parser

import org.omath.util._

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