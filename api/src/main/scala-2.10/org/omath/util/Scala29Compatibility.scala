package org.omath.util

object Scala29Compatibility {
  val +: = scala.collection.+:
  def ??? = scala.Predef.???
  val language = scala.language
  def future[T](body: => T)(implicit execctx: _root_.scala.concurrent.ExecutionContext = _root_.scala.concurrent.defaultExecutionContext): _root_.scala.concurrent.Future[T] = scala.concurrent.future(body)(execctx)
}