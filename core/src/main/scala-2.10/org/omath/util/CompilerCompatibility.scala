package org.omath.util

import scala.tools.nsc.Settings
import java.io.PrintWriter

object CompilerCompatibility {
  class TweakedIMain(settings: Settings, writer: PrintWriter) extends tools.nsc.interpreter.IMain(settings, writer) {
    def lastResult = valueOfTerm(mostRecentVar).get
  }
}