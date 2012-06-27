package org.omath.core.algebra

import org.omath.Expression
import org.omath.IntExpression
import org.omath.IntegerExpression
import org.omath.symbols
import org.apfloat.ApintMath

object Expand {
  def expandPower(terms: Seq[Expression], power: Int): Expression = {
    def multinomials(n: Int, k: Int): Iterator[(IntegerExpression, List[Int])] = Compositions(n, k).map(c => (multinomial(c), c)).iterator
    def multinomial(c: List[Int]): IntegerExpression = {
      import org.apfloat.Apint
      IntegerExpression(ApintMath.factorial(c.sum).divide(c.map(i => ApintMath.factorial(i)).reduce(_.multiply(_))))
    }

    symbols.Plus((for ((coefficient, powers) <- multinomials(power, terms.size)) yield {
      symbols.Times((coefficient match {
        case IntExpression(1) => Seq()
        case c => Seq(c)
      }) ++
        terms.zip(powers).collect({ case (x, 1) => x; case (x, p) if p > 1 => symbols.Power(x, p) }): _*)
    }).toSeq: _*)
  }
  def expandProduct(terms: Seq[Seq[Expression]]): Expression = {
    symbols.Plus(
      terms.foldLeft[List[List[Expression]]](List(Nil))({ (f: List[List[Expression]], t: Seq[Expression]) =>
        f.flatMap(x => t.map(_ :: x))
      }).map(f => symbols.Times(f: _*)): _*)
  }
}

object Compositions {
  private case class o(l: List[Int]) extends Odometer[List[Int]] {
    require(l.nonEmpty)

    override def increment(): List[Int] = {
      l.head + 1 :: l.tail
    }
    override def carry(): Option[List[Int]] = {
      // set the first nonzero entry to zero, increment the next entry
      l match {
        case 0 :: tail => (o(tail).carry) map { 0 :: _ }
        case a :: b :: tail => Some(0 :: (b + 1) :: tail)
        case _ => None
      }
    }
    override def reset = List.fill(l.size)(0)
  }

  def apply(n: Int, k: Int): Iterable[List[Int]] = {
    Odometer(List.fill(0)(k - 1))(o.apply _, { l: List[Int] => l.sum > n }).map(l => l :+ (n - l.sum))
  }
  def apply(n: Int, k: Int, maximum: List[Option[Int]]): Iterable[List[Int]] = {
    Odometer(List.fill(0)(k - 1))(o.apply _, { l: List[Int] =>
      l.sum > n || l.zipWithIndex.collect({ case (li, i) if maximum(i).map(_ < li).getOrElse(true) => i }).nonEmpty || maximum(k - 1).map(l.sum > n - _).getOrElse(false)
    }).map(l => l :+ (n - l.sum))
  }
}

trait Odometer[O] {
  def increment(): O
  def carry(): Option[O]
  def reset(): O
}

object Odometer {
  var counter = 0;

  private def successor[O](o: O)(implicit helper: O => Odometer[O], limit: O => Boolean): Option[O] = {
    val i = o.increment
    if (limit(i)) {
      Some(i)
    } else {
      carryRepeatedly(i)
    }
  }

  private def carryRepeatedly[O](o: O)(implicit helper: O => Odometer[O], limit: O => Boolean): Option[O] = {
    if (limit(o)) {
      Some(o)
    } else {
      o.carry match {
        case Some(c) => carryRepeatedly(c)
        case None => None
      }
    }
  }

  def apply[O](initial: O)(implicit helper: O => Odometer[O], limit: O => Boolean): Iterable[O] = {
    new Iterable[O] {
      def iterator = new Iterator[O] {

        var n: Option[O] = if (limit(initial)) {
          Some(initial)
        } else {
          successor(initial)
        }

        def next = {
          counter = counter + 1
          n match {
            case Some(o) => {
              n = successor(o)
              o
            }
            case None => throw new NoSuchElementException
          }
        }

        def hasNext = {
          n match {
            case Some(o) => true
            case None => false
          }
        }
      }
    }

  }
}
