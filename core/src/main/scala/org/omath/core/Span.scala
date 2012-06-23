package org.omath.core

import org.omath.{ symbols => systemSymbols }
import org.omath.IntExpression
import org.omath.bootstrap.conversions.Converter

case class Span(start: Int, finish: Int, step: Int = 1) {
  import org.omath.util.Scala29Compatibility.???
  def shift(k: Int): Span = {
    if (start < 0) require(start + k < 0)
    if (finish < 0) require(finish + k < 0)
    Span(start + k, finish + k, step)
  }

  def take[T](iterator: Iterator[T]): Iterator[T] = {
    if (start == 1) {
      if (finish > 0) {
        if (step == 1) {
          iterator.take(finish)
        } else {
          ???
        }
      } else if (finish == -1) {
        if (step == 1) {
          iterator
        } else {
          ???
        }
      } else {
        // have to perform look ahead
        ???
      }
    } else if (start > 0) {
      for (i <- 1 until start) iterator.hasNext
      shift(start - 1).take(iterator)
    } else {
      // have to go all the way to the end anyway
      take(iterator.toSeq).iterator
    }
  }

  def take[T](seq: Seq[T]): Seq[T] = ???
}
object Span {
  def apply(n: Int): Span = {
    if (n >= 0) {
      Span(start = 1, finish = n)
    } else {
      Span(start = n, finish = -1)
    }
  }

  val all = Span(1, -1)

  Converter.registerConversionToInstance({
    case (IntExpression(n), "org.omath.core.Span") => apply(n)
    case (systemSymbols.Infinity, "org.omath.core.Span") => all
  })

}