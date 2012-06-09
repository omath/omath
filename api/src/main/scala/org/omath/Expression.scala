package org.omath

import org.apfloat.Apint
import org.apfloat.Apfloat

trait Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): Expression
}

object Bindable extends IntegerExpressionImplicits with RealExpressionImplicits with StringExpressionImplicits with SymbolExpressionImplicits

trait Expression extends Bindable {
  def head: Expression
  def headDepth: Int // how many heads to you need to take to get a SymbolExpression
  def symbolHead: SymbolExpression
  def apply(arguments: Expression*): Expression = FullFormExpression(this, arguments.toList)
  def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression]
  final def bind(binding: Map[SymbolExpression, Expression]): Expression = bindOption(binding).getOrElse(this)
  
  def :>(bindable: Bindable) = patterns.ReplacementRule(this, bindable)
}

trait RawExpression extends Expression {
  override def head: SymbolExpression
  override def headDepth = 1
  override def symbolHead = head
}
trait LiteralExpression extends RawExpression {
  override def bindOption(binding: Map[SymbolExpression, Expression]) = None
}

trait SymbolExpression extends RawExpression {
  def context: Context
  def name: String
  override val head = symbols.Symbol
  override def equals(other: Any) = {
    other match {
      case other: SymbolExpression => name == other.name && context == other.context
      case _ => false
    }
  }
  override def hashCode = (name, context).hashCode

  override def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression] = {
    binding.get(this)
  }

  override def toString = name
}

protected trait SymbolExpressionImplicits {
  import language.implicitConversions
  implicit def scalaSymbol2Expression(s: Symbol): SymbolExpression = SymbolExpression(s)
}
object SymbolExpression {
  def apply(s: Symbol): SymbolExpression = SymbolExpression(s.toString.stripPrefix("'"))
  def apply(name: String, context: Context = Context.global): SymbolExpression = SymbolExpression_(name, context)
  def apply(name: String, context: String): SymbolExpression = apply(name, Context(context.split('`')))
  def apply(name: String, context: Seq[String]): SymbolExpression = apply(name, Context(context.flatMap(_.split('`'))))

  private case class SymbolExpression_(name: String, context: Context) extends SymbolExpression {
    try {
      require(name.nonEmpty)
      require(name.head.isLetter || name.head == '$')
      for (c <- name) require(c.isLetterOrDigit || c == '$')
    } catch {
      case e => throw new SymbolFormatException("'" + name + "' is not a valid omath symbol name.")
    }
  }

  class SymbolFormatException(message: String) extends Exception(message)
}

case class StringExpression(contents: String) extends LiteralExpression {
  override def equals(other: Any) = {
    other match {
      case other: StringExpression => contents == other.contents
      case _ => false
    }
  }
  override def hashCode = contents.hashCode

  override def toString = "\"" + contents + "\""
  override val head = symbols.String
}

trait StringExpressionImplicits {
  import language.implicitConversions
  implicit def string2StringExpression(s: String) = StringExpression(s)  
}
object StringExpression extends StringExpressionImplicits

trait IntegerExpression extends LiteralExpression {
  def toInt: Int
  def toLong: Long
  def toApint: Apint

  override val head = symbols.Integer

  override def toString = toApint.toString
  override def equals(other: Any) = {
    other match {
      case other: IntegerExpression => toApint == other.toApint
      case _ => false
    }
  }
  override def hashCode = toApint.hashCode
}
trait IntegerExpressionImplicits {
  import language.implicitConversions
  implicit def apply(i: Int): IntegerExpression = ApintExpression(new Apint(i))
  implicit def apply(i: Long): IntegerExpression = ApintExpression(new Apint(i))
  implicit def apply(i: BigInt): IntegerExpression = ApintExpression(new Apint(i.underlying))
  implicit def apply(i: Apint): IntegerExpression = ApintExpression(i)
}

//private case class IntExpression(toInt: Int) extends IntegerExpression {
//  def toLong = toInt.toLong
//  def toBigInt = BigInt(toInt)
//}
//private case class LongExpression(toLong: Long) extends IntegerExpression {
//  def toInt = toLong.toInt
//  def toBigInt = BigInt(toLong)
//}
//private case class BigIntExpression(toBigInt: BigInt) extends IntegerExpression {
//  def toInt = toBigInt.intValue
//  def toLong = toBigInt.longValue
//}

private case class ApintExpression(toApint: Apint) extends IntegerExpression {
  def toInt = toApint.intValue
  def toLong = toApint.longValue
}

object IntegerExpression extends IntegerExpressionImplicits 

trait RealExpression extends LiteralExpression {
  def toFloat: Float
  def toDouble: Double
  def toApfloat: Apfloat

  override val head = symbols.Real

  override def toString = toApfloat.toString
  override def equals(other: Any) = {
    other match {
      case other: RealExpression => toApfloat == other.toApfloat
      case _ => false
    }
  }
  override def hashCode = toApfloat.hashCode
}
trait RealExpressionImplicits {
  import language.implicitConversions
  implicit def apply(i: Float): RealExpression = ApfloatExpression(new Apfloat(i))
  implicit def apply(i: Double): RealExpression = ApfloatExpression(new Apfloat(i))
  implicit def apply(i: BigDecimal): RealExpression = ApfloatExpression(new Apfloat(i.toString))
  implicit def apply(i: Apfloat): RealExpression = ApfloatExpression(i)
}
object RealExpression extends RealExpressionImplicits

private case class ApfloatExpression(toApfloat: Apfloat) extends RealExpression {
  def toFloat = toApfloat.floatValue
  def toDouble = toApfloat.doubleValue
}

// TODO maybe this should just be a trait with an extractor on the companion object
case class FullFormExpression(head: Expression, arguments: Seq[Expression]) extends Expression {
  override def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression] = {
    head.bindOption(binding) match {
      case None => {
        // TODO this could be optimized quite a bit; no need to construct intermediate zips, in particular.
        val boundArguments = arguments.map(_.bindOption(binding))
        if (boundArguments.foldLeft(true)(_ && _.isEmpty)) {
          None
        } else {
          Some(head.apply(boundArguments.zip(arguments).map({ case (b, a) => b.getOrElse(a) }): _*))
        }
      }
      case Some(newHead) => Some(newHead.apply(arguments.map(_.bind(binding)): _*))
    }
  }

  override def headDepth = head.headDepth + 1
  override def symbolHead = head match {
    case head: SymbolExpression => head
    case _ => head.symbolHead
  }
  
  override def toString = head.toString + arguments.mkString("[", ", ", "]")
}

