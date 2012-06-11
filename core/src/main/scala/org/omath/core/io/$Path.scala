package org.omath.core.io

import scala.collection.JavaConversions

object $Path {
  // FIXME this needs to be much more robust!
  def apply(): Seq[String] = {
    import JavaConversions._
    val resources: Iterator[_root_.java.net.URL] = this.getClass.getClassLoader.getResources("")
    val coreClasses = resources.map(_.toString).filter(_.contains("/core/")).next
    Seq(coreClasses.take(coreClasses.indexOf("/core/") + 6) + "src/main/omath/")
  }
}