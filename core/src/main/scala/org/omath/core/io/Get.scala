package org.omath.core.io

import org.omath.kernel.Kernel
import scala.collection.JavaConversions
import java.net.URL
import java.net.URI
import org.omath.bootstrap.ClassLoaders
import net.tqft.toolkit.Logging

object Get extends Logging {
  def apply(path: String)(implicit kernel: Kernel) {
    if (path.startsWith("file:/") || path.startsWith("http:/") || path.startsWith("https:/")) {
      kernel.slurp(new java.net.URL(path))
    } else {
      (for (place <- "" +: $Path()) yield {
        if (place.startsWith("file:")) {
          val file = new java.io.File(place.stripPrefix("file:"), path)
          if (file.exists && file.isFile) {
            Seq(file.toURI.toURL)
          } else {
            Seq.empty
          }
        } else {
          import JavaConversions._
          ClassLoaders.getResources(place + path).toSeq
        }
      }).flatten.headOption match {
        case Some(found) => {
          info("Get[" + path + "] now slurping from " + found)
          kernel.slurp(found)
        }
        case None => {
          // TODO complain properly
          System.err.println("File not found: " + path)
        }
      }
    }
  }
}