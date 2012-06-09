package org.omath.core

import org.omath.Expression
import org.omath.SymbolExpression
import org.omath.kernel.Kernel
import org.omath.kernel.ParsingKernel
import scala.collection.JavaConversions

trait Core extends ParsingKernel { kernel: Kernel =>
    // FIXME look at $Context and $ContextPath
  override protected def symbolizer = { s: String => SymbolExpression(s, "System") }
  
  {
    import JavaConversions._
    val resources: Iterator[java.net.URL] = this.getClass.getClassLoader.getResources("")
    val coreClasses = resources.map(_.toString).filter(_.contains("/core/")).next
    val `core.m` = coreClasses.take(coreClasses.indexOf("/core/") + 6) + "src/main/omath/core.m"
    slurp(new java.net.URI(`core.m`))
  }
}

