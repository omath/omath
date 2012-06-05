package scala.collection

object +: {
  def unapply[T,Coll <: SeqLike[T, Coll]](
      t: Coll with SeqLike[T, Coll]): Option[(T, Coll)] =
    t.headOption map ( h => (h, t.tail))
}