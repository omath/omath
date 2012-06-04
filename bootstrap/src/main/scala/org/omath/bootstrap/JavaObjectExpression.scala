package org.omath.bootstrap

import org.omath._
import java.lang.reflect.Method

class JavaObjectExpression[T](val contents: T) extends LiteralExpression {
  override def toString = "JavaObject[" + contents.hashCode + "]"
}
object JavaObjectExpression {
  def apply[T](contents: T) = new JavaObjectExpression[T](contents)
}

case class JavaClassExpression[T](contents: Class[T]) extends JavaObjectExpression[Class[T]](contents) {
  override def toString = "JavaClass[\"" + contents.getName + "\"]"
}

case class JavaMethodExpression[T](contents: Method) extends JavaObjectExpression[Method](contents) {
  override def toString = "JavaClass[\"" + contents.getDeclaringClass.getName + "\", \"" + contents.getName + "\"]"
}