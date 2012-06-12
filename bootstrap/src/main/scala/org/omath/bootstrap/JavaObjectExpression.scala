package org.omath.bootstrap

import org.omath._
import java.lang.reflect.Method

// can't be a case class only because we want subtypes below...
class JavaObjectExpression[T](val contents: T) extends LiteralExpression {
  override def toString = "JavaObject[\"" + contents.getClass.getCanonicalName + "\", " + contents.hashCode + "]"
  override val head = bootstrap.symbols.JavaObject
}
object JavaObjectExpression {
  def apply[T](contents: T) = new JavaObjectExpression[T](contents)
}

case class JavaClassExpression(override val contents: Class[_]) extends JavaObjectExpression[Class[_]](contents) {
  override def toString = "JavaClass[\"" + contents.getName + "\"]"
  override val head = bootstrap.symbols.JavaClass
}

case class JavaMethodExpression(override val contents: Method) extends JavaObjectExpression[Method](contents) {
  override def toString = "JavaMethod[\"" + contents.getDeclaringClass.getName + "\", \"" + contents.getName + "\"]"
  override val head = bootstrap.symbols.JavaMethod
}