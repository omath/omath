package org.omath.patterns

import org.omath.SymbolExpression

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
        combine(x.tryCompare(a, b), y.tryCompare(a, b))
      }
    }
  }
  def combine(x: Option[Int], y: Option[Int]) = {
    x match {
      case None => None
      case Some(0) => y
      case Some(n) if n > 0 => {
        y match {
          case Some(n) if n >= 0 => Some(1)
          case _ => None
        }
      }
      case Some(n) if n < 0 => {
        y match {
          case Some(n) if n <= 0 => Some(-1)
          case _ => None
        }
      }
    }
  }
}

trait PatternComparator extends PartialOrdering[Pattern] {
  // the discrete partial ordering
  //  override def tryCompare(a: Pattern, b: Pattern): Option[Int] = {
  //    if (a == b) {
  //      Some(0)
  //    } else {
  //      None
  //    }
  //  }

  import org.omath.util.Scala29Compatibility.???

  def tryCompare(a: Pattern, b: Pattern): Option[Int] = FancyComparator.tryCompare((Nil, a), (Nil, b))

  // The FancyComparator keeps track of names which have previously appeared.
  // (we need to do this to be able to say that {x_, x_} is a more specific pattern than {x_, y_}
  private object FancyComparator extends PartialOrdering[(Seq[SymbolExpression], Pattern)] {
    def tryCompare(pa: (Seq[SymbolExpression], Pattern), pb: (Seq[SymbolExpression], Pattern)): Option[Int] = {
      def switch = tryCompare(pb, pa).map(-1 * _)

      val a = pa._2
      val b = pb._2
      val sa = pa._1
      val sb = pb._1
      (a, b) match {
        case (a: AlternativesPattern, b: AlternativesPattern) => {
          ???
        }
        case (a: AlternativesPattern, b) => switch
        case (a, b: AlternativesPattern) => {
        	val r = b.alternatives.map(b2 => tryCompare(pa, (sb, b2)))
        	if(r.contains(None)) {
        	  None
        	} else {        	          	  
        	  if(r.forall(_.get == 0)) {
        	    Some(0)
        	  } else if(r.forall(_.get <= 0)) {
        	    Some(-1)
        	  } else if(r.forall(_.get >=0)) {
        	    Some(1)
        	  } else {
        	    None
        	  }
        	}
        }
        case (a: GenericBlank, b: GenericBlank) => {
          PartialOrderingHelper.combine(Some(a.length.compare(b.length)), HeadComparator.tryCompare(a.head, b.head))
        }
        case (a: GenericBlank, b) => switch
        case (a, b: GenericBlank) => {
          // TODO
          ???
        }
        // Neither are a blank.
        case (a: NamedPattern, b) => {
          // FIXME ignore names for now
          tryCompare((a.name +: pa._1, a.inner), pb)
        }
        case (a, b: NamedPattern) => {
          switch
        }

        // TODO
        // Final catch all case.
        case _ => {
          if (a == b) {
            Some(0)
          } else {
            None
          }
        }
      }
    }

  }
}