package org.omath.core.io

import org.omath.kernel.ParsingKernel

object Get {
  def apply(path: String)(implicit kernel: ParsingKernel) {
    if (path.startsWith("file:/") || path.startsWith("http:/") || path.startsWith("https:/")) {
      kernel.slurp(new java.net.URI(path))
    } else {
      (for (
          place <- $Path();
          if(place.startsWith("file:"));
          file = new java.io.File(place.stripPrefix("file:"), path);
          if file.exists && file.isFile
          ) yield file).headOption match {
            case Some(found) => kernel.slurp(found)
            case None => {
              // TODO complain properly
              System.err.println("File not found: " + path)
            }
          }
    }
  }
}