package org.omath.patterns

trait PartialOrdering[T] extends scala.math.PartialOrdering[T] {
   override def lteq(x: T, y: T) = {
    tryCompare(x, y).map(_ <= 0).getOrElse(false)
  } 
}

object PartialOrderingHelper {
  implicit def orderingToPartialOrdering[T](ord: Ordering[T]): PartialOrdering[T] = {
    new PartialOrdering[T] {
      override def tryCompare(a: T, b: T) = Some(ord.compare(a, b))
    }
  }
  def product[T](x: PartialOrdering[T], y: PartialOrdering[T]): PartialOrdering[T] = {
    new PartialOrdering[T] {
      override def tryCompare(a: T, b: T) = {
        x.tryCompare(a, b) match {
          case None => None
          case Some(0) => y.tryCompare(a, b)
          case Some(n) if n > 0 => {
            if(y.gteq(a, b)) Some(1) else None
          }
          case Some(n) if n < 0 => {
            if(y.lteq(a, b)) Some(-1) else None
          }
        }
      }
    }
  }
}

trait PatternComparator extends PartialOrdering[Pattern] {
  // the discrete partial ordering
  override def tryCompare(a: Pattern, b: Pattern): Option[Int] = {
    if (a == b) {
      Some(0)
    } else {
      None
    }
  }

  import org.omath.util.Scala29Compatibility.???
  
  /* override */ def tryCompare2(a: Pattern, b: Pattern): Option[Int] = {
    (a, b) match {
      case (a: GenericBlank, b: GenericBlank) => {
        ???
      }
    }
  }
  
}