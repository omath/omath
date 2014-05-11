package org.omath.parser

import org.omath.expression._
import scala.util.Try
import scala.util.Success

trait SyntaxParserImplementation extends SyntaxParser {
  override def parseSyntax[E](syntax: String)(implicit builder: ExpressionBuilder[E]): Try[E] = {
    Syntax2FullFormParser(syntax).flatMap(FullFormParser(_))
  }

  override def parseSyntax[E](lines: Iterator[String])(implicit builder: ExpressionBuilder[E]): Iterator[Try[E]] = {
    new Iterator[Try[E]] {
      val cache = scala.collection.mutable.ListBuffer[String]()
      def parseCache = parseSyntax(cache.mkString(" "))
      var nextOption: Option[Try[E]] = produceNextOption
      @scala.annotation.tailrec
      private def produceNextOption: Option[Try[E]] = {
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

object SyntaxParserImplementation extends SyntaxParserImplementation