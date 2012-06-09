package org.omath.bootstrap

import org.omath._
import org.omath.kernel.Kernel
import java.lang.reflect.Method
import org.omath.bootstrap.conversions.Converter

case object ClassForNameBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaClassExpression = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
    }

    JavaClassExpression(clazz)
  }
}

case object GetClassBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaClassExpression = {
    val clazz: Class[_] = binding('object) match {
      case obj: JavaObjectExpression[_] => obj.contents.getClass
    }

    JavaClassExpression(clazz)
  }
}

case object JavaMethodBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaMethodExpression = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
      case javaObject: JavaObjectExpression[_] => javaObject.contents match {
        case clazz: Class[_] => clazz
      }
    }

    val methodName = binding('method).asInstanceOf[StringExpression].contents

    JavaMethodExpression(clazz.getMethods.find(_.getName == methodName).get)
  }
}

trait Boxing {
  def box(arguments: Seq[Expression], classes: Seq[Class[_]]): Option[Seq[Object]] = {
    if (arguments.size != classes.size) {
      None
    } else {
      val options: Seq[Option[Object]] = arguments.zip(classes).map({ p => Converter.fromExpression(p._1, p._2).asInstanceOf[Option[Object]] })
      if (options.exists(_.isEmpty)) {
        None
      } else {
        Some(options.map(_.get))
      }
    }
  }
  def unbox(obj: Object): Expression = {
    Converter.toExpression(obj)
  }
}

case object MethodInvocationBindable extends Bindable with Boxing {
  def bind(binding: Map[SymbolExpression, Expression]): Expression = {
    val method = binding('method).asInstanceOf[JavaMethodExpression].contents
    val o = binding('object) match {
      case org.omath.symbols.Null => null
      case j: JavaObjectExpression[_] => j.contents
    }
    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments

    unbox(method.invoke(o, box(arguments, method.getParameterTypes).get: _*))

  }
}

case object JavaNewBindable extends Bindable with Boxing {
  def bind(binding: Map[SymbolExpression, Expression]): Expression = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
      case javaObject: JavaObjectExpression[_] => javaObject.contents match {
        case clazz: Class[_] => clazz
      }
    }

    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments

    (for (
      constructor <- clazz.getConstructors.view;
      boxedArguments <- box(arguments, constructor.getParameterTypes)
    ) yield unbox(constructor.newInstance(boxedArguments: _*).asInstanceOf[Object])).head

  }
}

case class SetDelayedBindable(kernel: Kernel) extends Bindable {
  private object SubValueAttachesTo {
    @scala.annotation.tailrec
    final def unapply(x: FullFormExpression): Option[SymbolExpression] = {
      import org.omath.symbols.{ Pattern, Blank }
      x.head match {
        case s: SymbolExpression => None
        case Pattern(_, Blank(s: SymbolExpression)) => Some(s)
        case FullFormExpression(s: SymbolExpression, _) => Some(s)
        case h: FullFormExpression => unapply(h)
      }
    }
  }

  def bind(binding: Map[SymbolExpression, Expression]): SymbolExpression = {
    val lhs = binding('lhs)
    val rhs = binding('rhs)
    lhs match {
      case lhs: SymbolExpression => kernel.kernelState.addOwnValues(lhs, lhs :> rhs)
      case lhs @ FullFormExpression(s: SymbolExpression, _) => kernel.kernelState.addDownValues(s, lhs :> rhs)
      case SubValueAttachesTo(s) => kernel.kernelState.addSubValues(s, lhs :> rhs)
      case lhs: FullFormExpression => kernel.kernelState.addSubValues(lhs.symbolHead, lhs :> rhs)
    }

    org.omath.symbols.Null
  }
}