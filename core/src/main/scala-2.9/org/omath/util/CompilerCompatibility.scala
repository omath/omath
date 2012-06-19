package org.omath.util

import scala.tools.nsc.Settings
import java.io.PrintWriter

object CompilerCompatibility {
  
  // use the hack from https://issues.scala-lang.org/browse/SI-4899
  class TweakedIMain(settings: Settings, writer: PrintWriter) extends tools.nsc.interpreter.IMain(settings, writer) {
    def lastRequest = prevRequestList.last
    def lastResult = lastRequest.lineRep.call("$result")
  }
}