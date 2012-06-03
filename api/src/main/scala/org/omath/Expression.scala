package org.omath

trait Expression {
  def apply(arguments: Expression*): Expression = FullFormExpression(this, arguments.toList)
  def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression]
  final def bind(binding: Map[SymbolExpression, Expression]): Expression = bindOption(binding).getOrElse(this) 
}
object Expression {
  import SymbolExpression._
  implicit def scalaSymbol2Expression(s: Symbol): SymbolExpression = s
  
  import IntegerExpression._
  implicit def int2IntegerExpression(i: Int): IntegerExpression = i
  implicit def long2IntegerExpression(i: Long): IntegerExpression = i
  implicit def bigint2IntegerExpression(i: BigInt): IntegerExpression = i
  
  implicit def string2StringExpression(s: String): StringExpression = StringExpression(s)  
  
  implicit def seq2ListExpression(s: Seq[Expression]): Expression = symbols.List(s:_*)
}

trait RawExpression extends Expression
trait LiteralExpression extends RawExpression {
  override def bindOption(binding: Map[SymbolExpression, Expression]) = None
}

trait SymbolExpression extends RawExpression {
  def context: Context
  def name: String
  
  override def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression] = {
    binding.get(this)
  }
}
object SymbolExpression {
  implicit def scalaSymbol2Expression(s: Symbol): SymbolExpression = {
    s.toString.stripPrefix("'").split('`').toList match {
      case name :: Nil => GlobalSymbolExpression(name)
      case qualifiedName => SymbolExpression_(Context(qualifiedName.init), qualifiedName.last)
    }
  }
  def apply(name: String, context: Context = Context.global): SymbolExpression = SymbolExpression_(context, name)
  
  private case class SymbolExpression_(context: Context, name: String) extends SymbolExpression
}
case class GlobalSymbolExpression(override val name: String) extends SymbolExpression {
  override val context = Context.global
}
case class SystemSymbolExpression(override val name: String) extends SymbolExpression {
  override val context = Context.system
}

case class StringExpression(contents: String) extends LiteralExpression
object StringExpression {
  implicit def string2StringExpression(s: String) = StringExpression(s)
}

trait IntegerExpression extends LiteralExpression {
  def toInt: Int
  def toLong: Long
  def toBigInt: BigInt
}
object IntegerExpression {
  implicit def apply(i: Int): IntegerExpression = IntExpression(i)
  implicit def apply(i: Long): IntegerExpression = LongExpression(i)
  implicit def apply(i: BigInt): IntegerExpression = BigIntExpression(i)

  private case class IntExpression(toInt: Int) extends IntegerExpression {
    def toLong = toInt.toLong
    def toBigInt = BigInt(toInt)
  }
  private case class LongExpression(toLong: Long) extends IntegerExpression {
    def toInt = toLong.toInt
    def toBigInt = BigInt(toLong)
  }
  private case class BigIntExpression(toBigInt: BigInt) extends IntegerExpression {
    def toInt = toBigInt.intValue
    def toLong = toBigInt.longValue
  }
}
trait RealExpression extends LiteralExpression {
  def toFloat: Float
  def toDouble: Double
  def toBigDecimal: BigDecimal
}

case class FullFormExpression(head: Expression, arguments: List[Expression]) extends Expression {
  override def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression] = {
    head.bindOption(binding) match {
      case None => {
        // TODO this could be optimized quite a bit; no need to construct intermediate zips, in particular.
        val boundArguments = arguments.map(_.bindOption(binding))
        if(boundArguments.foldLeft(true)(_ && _.isEmpty)) {
          None
        } else {
          Some(head.apply(boundArguments.zip(arguments).map({ case (b, a) => b.getOrElse(a) }):_*))
        }
      }
      case Some(newHead) => Some(newHead.apply(arguments.map(_.bind(binding)):_*))
    }
  }
}

