package org.omath.parser

import org.omath.expression._
import scala.util.Try

trait SyntaxParser {
  def parseSyntax[E](syntax: String)(implicit builder: ExpressionBuilder[E]): Try[E]
  def parseSyntax[E](lines: Iterator[String])(implicit builder: ExpressionBuilder[E]): Iterator[Try[E]]
}
