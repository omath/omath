package org.omath

trait Context {
  def names: Seq[String]
  override def toString = names.mkString("", "`", "`")
}

object Context {
  val global = Context(List("Global"))
  val system = Context(List("System"))

  import org.omath.util.Scala29Compatibility._
  import language.implicitConversions
  implicit def apply(name: String): Context = {
    apply(name.stripSuffix("`").split('`').toList)
  }

  def apply(names: Seq[String]): Context = _Context(names)
}

private case class _Context(names: Seq[String]) extends Context
