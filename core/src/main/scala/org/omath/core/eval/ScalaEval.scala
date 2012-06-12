package org.omath.core.eval

import net.tqft.toolkit.eval.Eval
import net.tqft.toolkit.Logging

object ScalaEval extends Logging {
  def apply(code: String): Any = {
    info("evaluating '" + code + "' using the Scala REPL")
    Eval(code).get
  }
}