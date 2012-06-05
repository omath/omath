package org.omath.bootstrap

import org.omath._
import org.omath.kernel.Kernel
import java.lang.reflect.Method

case object JavaClassBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaObjectExpression[Class[_]] = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
    }

    JavaObjectExpression(clazz)
  }
}

case object JavaMethodBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaObjectExpression[Method] = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
      case javaObject: JavaObjectExpression[_] => javaObject.contents match {
        case clazz: Class[_] => clazz
      }
    }

    val methodName = binding('method).asInstanceOf[StringExpression].contents

    JavaObjectExpression(clazz.getMethods.find(_.getName == methodName).get)
  }
}

case object MethodInvocationBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaObjectExpression[_] = {
    val method = binding('method).asInstanceOf[JavaMethodExpression].contents
    val o = binding('object).asInstanceOf[JavaObjectExpression[_]].contents
    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments

    JavaObjectExpression(method.invoke(o, arguments: _*))
  }
}

case object StaticMethodInvocationBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaObjectExpression[_] = {
    val method = binding('method).asInstanceOf[JavaMethodExpression].contents
    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments

    JavaObjectExpression(method.invoke(null, arguments: _*))
  }
}

case object JavaNewBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaObjectExpression[_] = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
      case javaObject: JavaObjectExpression[_] => javaObject.contents match {
        case clazz: Class[_] => clazz
      }
    }

    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments

    JavaObjectExpression(clazz.getConstructor().newInstance(arguments: _*))
  }
}

case class SetDelayedBindable(kernel: Kernel) extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): SymbolExpression = {
    val lhs = binding('lhs)
    val rhs = binding('rhs)
    // FIXME, decide whether this should be a OwnValue, DownValue or SubValue
    lhs match {
      case lhs: SymbolExpression => kernel.kernelState.addOwnValues(lhs, lhs :> rhs)
    }
    
    org.omath.symbols.Null
  }
}