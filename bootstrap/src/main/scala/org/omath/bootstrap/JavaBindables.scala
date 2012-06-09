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

case object MethodInvocationBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): Expression = {
    val method = binding('method).asInstanceOf[JavaMethodExpression].contents
    val o = binding('object) match {
      case org.omath.symbols.Null => null
      case j: JavaObjectExpression[_] => j.contents
    }
    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments

    val boxedArguments = arguments.zip(method.getParameterTypes).map({ p => Converter.fromExpression(p._1, p._2).get.asInstanceOf[Object] })
    val result = method.invoke(o, boxedArguments: _*)
    Converter.toExpression(result)
  }
}

case object JavaNewBindable extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): Expression = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
      case javaObject: JavaObjectExpression[_] => javaObject.contents match {
        case clazz: Class[_] => clazz
      }
    }

    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments
    // TODO try all the constructors, looking for one that the Converter can manage
    val constructor = clazz.getConstructor()
    val boxedArguments = arguments.zip(constructor.getParameterTypes).map({ p => Converter.fromExpression(p._1, p._2).get.asInstanceOf[Object] })

    val result = clazz.getConstructor().newInstance(boxedArguments: _*)
    Converter.toExpression(result)
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