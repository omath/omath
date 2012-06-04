package org.omath.bootstrap

import org.omath._
import java.lang.reflect.Method

case object MethodReflection extends Bindable {
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

case object ClassReflection extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaObjectExpression[Class[_]] = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
    }

    JavaObjectExpression(clazz)
  }
}

case object MethodInvocation extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaObjectExpression[_] = {
    val method = binding('method).asInstanceOf[JavaMethodExpression].contents
    val o = binding('object).asInstanceOf[JavaObjectExpression[_]].contents
    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments

    JavaObjectExpression(method.invoke(o, arguments: _*))
  }
}

case object JavaNew extends Bindable {
  def bind(binding: Map[SymbolExpression, Expression]): JavaObjectExpression[_] = {
    val clazz: Class[_] = binding('class) match {
      case name: StringExpression => Class.forName(name.contents)
      case javaObject: JavaObjectExpression[_] => javaObject.contents match {
        case clazz: Class[_] => clazz
      }
    }

    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments
    
    JavaObjectExpression(clazz.getConstructor().newInstance(arguments:_*))
  }
}