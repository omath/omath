package org.omath

import org.apfloat.Apint
import org.apfloat.Apfloat
import org.omath.kernel.Evaluation

trait Bindable extends Serializable {
  def activeBind(binding: Map[SymbolExpression, Expression])(implicit evaluation: Evaluation): Expression
}
trait PassiveBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): Expression
  override def activeBind(binding: Map[SymbolExpression, Expression])(implicit evaluation: Evaluation): Expression = bind(binding)
}

object Bindable extends IntegerExpressionImplicits with RealExpressionImplicits with StringExpressionImplicits with SymbolExpressionImplicits

trait Expression extends PassiveBindable {
  def head: Expression
  def headDepth: Int // how many heads do you need to take to get a SymbolExpression
  def symbolHead: SymbolExpression
  def apply(arguments: Expression*): Expression = FullFormExpression(this, arguments.toList)

  def unapplySeq(x: FullFormExpression): Option[Seq[Expression]] = {
    if (x.head == this) {
      Some(x.arguments)
    } else {
      None
    }
  }

  def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression]
  override def bind(binding: Map[SymbolExpression, Expression]): Expression = bindOption(binding).getOrElse(this)

  def :>(bindable: Bindable)(implicit attributes: SymbolExpression => Seq[SymbolExpression]) = patterns.ReplacementRule(this, bindable)

  final override def toString = toContextualString(Seq(Context.global, Context.system))
  def toContextualString(symbolInContext: SymbolExpression => Boolean): String
  def toContextualString(contexts: Seq[Context]): String = toContextualString({ s: SymbolExpression => contexts.contains(s.context) })
  def toContextualString(implicit evaluation: Evaluation): String = {
    evaluation.evaluate(symbols.$ContextPath) match {
      case FullFormExpression(symbols.List, contexts) => {
        toContextualString(contexts collect { case StringExpression(contextName) => Context(contextName) })
      }
      case _ => toString
    }
  }
}

trait RawExpression extends Expression {
  override def head: SymbolExpression
  override def headDepth = 1
  override def symbolHead = head
}
trait LiteralExpression extends RawExpression {
  override def bindOption(binding: Map[SymbolExpression, Expression]) = None
  final override def toContextualString(symbolInContext: SymbolExpression => Boolean): String = toLiteralString
  def toLiteralString: String
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

  override def toContextualString(symbolInContext: SymbolExpression => Boolean) = if (symbolInContext(this)) {
    name
  } else {
    context.toString + name
  }
}

trait OneIdentity extends SymbolExpression {
  override def apply(arguments: Expression*) = {
    if (arguments.size == 1) {
      arguments.head
    } else {
      super.apply(arguments: _*)
    }
  }
}

protected trait SymbolExpressionImplicits {
  import org.omath.util.Scala29Compatibility._
  import language.implicitConversions
  implicit def scalaSymbol2Expression(s: Symbol): SymbolExpression = SymbolExpression(s)
}
object SymbolExpression {
  def apply(s: Symbol): SymbolExpression = SymbolExpression(s.toString.stripPrefix("'"))
  def apply(name: String, context: Context = Context.global): SymbolExpression = SymbolExpression_(name, context)
  def apply(name: String, context: String): SymbolExpression = apply(name, Context(context.split('`')))
  def apply(name: String, context: Seq[String]): SymbolExpression = apply(name, Context(context.flatMap(_.split('`'))))

  class SymbolFormatException(message: String) extends Exception(message)

  case class SymbolExpression_(name: String, context: Context) extends SymbolExpression {
    try {
      require(name.nonEmpty)
      require(name.head.isLetter || name.head == '$')
      for (c <- name) require(c.isLetterOrDigit || c == '$')
    } catch {
      case e => throw new SymbolExpression.SymbolFormatException("'" + name + "' is not a valid omath symbol name.")
    }
  }

}

case class StringExpression(contents: String) extends LiteralExpression {
  override def equals(other: Any) = {
    other match {
      case other: StringExpression => contents == other.contents
      case _ => false
    }
  }
  override def hashCode = contents.hashCode

  override def toLiteralString = "\"" + contents + "\""
  override val head = symbols.String
}

trait StringExpressionImplicits {
  import org.omath.util.Scala29Compatibility._
  import language.implicitConversions
  implicit def string2StringExpression(s: String) = StringExpression(s)
}
object StringExpression extends StringExpressionImplicits

trait IntegerExpression extends LiteralExpression {
  def toInt: Int
  def toLong: Long
  def toApint: Apint

  override val head = symbols.Integer

  override def toLiteralString = toApint.toString
  override def equals(other: Any) = {
    other match {
      case other: IntegerExpression => toApint == other.toApint
      case _ => false
    }
  }
  override def hashCode = toApint.hashCode
}
trait IntegerExpressionImplicits {
  import org.omath.util.Scala29Compatibility._
  import language.implicitConversions
  implicit def apply(i: Int): IntegerExpression = ApintExpression(new Apint(i))
  implicit def apply(i: Long): IntegerExpression = ApintExpression(new Apint(i))
  implicit def apply(i: BigInt): IntegerExpression = ApintExpression(new Apint(i.underlying))
  implicit def apply(i: Apint): IntegerExpression = ApintExpression(i)
}

private case class ApintExpression(toApint: Apint) extends IntegerExpression {
  def toInt = toApint.intValue
  def toLong = toApint.longValue
}

object IntegerExpression extends IntegerExpressionImplicits {
  def unapply(x: IntegerExpression) = Some(x.toApint)
}

object IntExpression {
  def unapply(x: IntegerExpression) = try {
    Some(x.toInt)
  } catch {
    case _ => None
  }
}

trait RealExpression extends LiteralExpression {
  def toFloat: Float
  def toDouble: Double
  def toApfloat: Apfloat

  override val head = symbols.Real

  override def toLiteralString = toApfloat.toString
  override def equals(other: Any) = {
    other match {
      case other: RealExpression => toApfloat == other.toApfloat
      case _ => false
    }
  }
  override def hashCode = toApfloat.hashCode
}
trait RealExpressionImplicits {
  import org.omath.util.Scala29Compatibility._
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

trait FullFormExpression extends Expression {
  def arguments: Seq[Expression]
  private def bindArguments(binding: Map[SymbolExpression, Expression], arguments: Seq[Expression]): Option[Seq[Expression]] = {
    val boundArguments = arguments.map(_.bindOption(binding))
    if (boundArguments.forall(_.isEmpty)) {
      None
    } else {
      // TODO this could be optimized quite a bit; no need to construct intermediate zips, in particular.
      Some(boundArguments.zip(arguments).flatMap({
        case (Some(FullFormExpression(symbols.Sequence, elements)), _) => elements
        case (Some(element), _) => Seq(element)
        case (None, a) => Seq(a)
      }))
    }
  }

  override def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression] = {
    (head.bindOption(binding), bindArguments(binding, arguments)) match {
      case (None, None) => None
      case (None, Some(newArguments)) => Some(head(newArguments: _*))
      case (Some(newHead), None) => Some(newHead(arguments: _*))
      case (Some(newHead), Some(newArguments)) => Some(newHead(newArguments: _*))
    }
  }

  override def headDepth = head.headDepth + 1
  override def symbolHead = head match {
    case head: SymbolExpression => head
    case _ => head.symbolHead
  }

  override def toContextualString(symbolInContext: SymbolExpression => Boolean) = {
    head.toContextualString(symbolInContext) + arguments.map(_.toContextualString(symbolInContext)).mkString("[", ", ", "]")
  }
}

object FullFormExpression {
  def apply(head: Expression, arguments: Seq[Expression]) = _FullFormExpression(head, arguments)
  def unapply(expression: FullFormExpression) = Some((expression.head, expression.arguments))
}

case class _FullFormExpression(override val head: Expression, override val arguments: Seq[Expression]) extends FullFormExpression
