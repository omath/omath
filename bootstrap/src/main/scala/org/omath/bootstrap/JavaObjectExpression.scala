package org.omath.bootstrap

import scala.language.existentials 

import org.omath._
import java.lang.reflect.Method

// can't be a case class only because we want subtypes below...
class JavaObjectExpression[T](val contents: T) extends LiteralExpression {
  override def toLiteralString = "JavaObject[\"" + contents.getClass.getName + "\", " + contents.hashCode + "]"
  override val head = bootstrap.symbols.JavaObject
  override val hashCode = contents.hashCode + 1
  override def equals(other: Any) = {
    other match {
      case other: JavaObjectExpression[_] => other.contents == contents
      case _ => false
    }
  }
}
object JavaObjectExpression {
  def apply[T](contents: T) = new JavaObjectExpression[T](contents)
}

case class JavaClassExpression(override val contents: Class[_]) extends JavaObjectExpression[Class[_]](contents) {
  override def toLiteralString = "JavaClass[\"" + contents.getName + "\"]"
  override val head = bootstrap.symbols.JavaClass
}

case class JavaMethodExpression(override val contents: Method) extends JavaObjectExpression[Method](contents) {
  override def toLiteralString = "JavaMethod[\"" + contents.getDeclaringClass.getName + "\", \"" + contents.getName + "\"]"
  override val head = bootstrap.symbols.JavaMethod
}