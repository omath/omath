package org.omath.ui.applet

import java.applet._
import java.awt._
import org.omath.core.TungstenCore
import java.security.AccessController
import java.security.PrivilegedAction

class omath extends Applet {
  import language.implicitConversions

  implicit def any2PrivilegedAction[T](x: => T) = new PrivilegedAction[T] {
    override def run = x
  }
  def privileged[T](x: => T): T = AccessController.doPrivileged(x)

  def evaluateSyntax(syntax: String) = {
    privileged {
      TungstenCore.evaluateSyntax(syntax).get.toString
    }
  }
}
