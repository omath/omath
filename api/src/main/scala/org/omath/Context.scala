package org.omath

case class Context(names: Seq[String])

object Context {
  val global = Context(List("Global"))
  val system = Context(List("System"))
}

