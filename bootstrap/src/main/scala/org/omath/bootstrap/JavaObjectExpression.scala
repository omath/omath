package org.omath.bootstrap

import org.omath._
import java.lang.reflect.Method

class JavaObjectExpression[T](val contents: T) extends LiteralExpression {
  override def toString = "JavaObject[" + contents.hashCode + "]"
  override val head = bootstrap.symbols.JavaObject
}
object JavaObjectExpression {
  def apply[T](contents: T) = new JavaObjectExpression[T](contents)
}

case class JavaClassExpression[T](override val contents: Class[T]) extends JavaObjectExpression[Class[T]](contents) {
  override def toString = "JavaClass[\"" + contents.getName + "\"]"
}

case class JavaMethodExpression(override val contents: Method) extends JavaObjectExpression[Method](contents) {
  override def toString = "JavaMethod[\"" + contents.getDeclaringClass.getName + "\", \"" + contents.getName + "\"]"
}