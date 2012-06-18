package org.omath.util

import scala.collection.SeqLike

object Scala29Compatibility {
  // from http://jsuereth.com/scala/2011/12/28/pluscolon-extractor.html
  object +: {
    def unapply[T, Coll <: SeqLike[T, Coll]](
      t: Coll with SeqLike[T, Coll]): Option[(T, Coll)] =
      t.headOption map (h => (h, t.tail))
  }

  def ??? = throw new NoSuchMethodError

  object language {
    val implicitConversions = ???
  }

  def future[T](body: => T): _root_.scala.actors.Future[T] = _root_.scala.actors.Futures.future(body)

}