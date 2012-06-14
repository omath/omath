package org.omath.core.io

import scala.collection.JavaConversions
import net.tqft.toolkit.Logging
import org.omath.bootstrap.ClassLoaders

object $Path extends Logging {
  lazy val guess = {
    (for (
      pathURI <- ClassLoaders.getResources("");
      path = pathURI.toString;
      if (path.contains("/core/"))
    ) yield {
      path.take(path.indexOf("/core/") + 6) + "src/main/omath/"
    }).toSeq.distinct
  }

  def apply(): Seq[String] = guess
}