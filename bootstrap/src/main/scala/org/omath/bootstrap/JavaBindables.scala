package org.omath.bootstrap

import org.omath._
import org.omath.kernel.Kernel
import java.lang.reflect.Method

case object JavaClassBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaClassExpression = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
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
    lhs match {
      case lhs: SymbolExpression => kernel.kernelState.addOwnValues(lhs, lhs :> rhs)
      case lhs @ FullFormExpression(s: SymbolExpression, _) => kernel.kernelState.addDownValues(s, lhs :> rhs)
      case lhs: FullFormExpression => kernel.kernelState.addSubValues(lhs.symbolHead, lhs :> rhs)
    }
    
    org.omath.symbols.Null
  }
}