package org.omath.ui.applet

import java.applet._
import java.awt._
import org.omath.core.TungstenCore
import java.security.AccessController
import java.security.PrivilegedAction

class omath extends Applet {
  import org.omath.util.Scala29Compatibility.language
  import language.implicitConversions

  implicit def any2PrivilegedAction[T](x: => T) = new PrivilegedAction[T] {
    override def run = x
  }
  def privileged[T](x: => T): T = AccessController.doPrivileged(x)

  
  privileged { TungstenCore }
  
  def evaluateSyntax(syntax: String) = {
    try {
      privileged {
        TungstenCore.evaluateSyntax(syntax).get.toString
      }
    } catch {
      case e => {
        e.toString + "\n" + e.getStackTrace.mkString("\n")
      }
    }
  }
}
