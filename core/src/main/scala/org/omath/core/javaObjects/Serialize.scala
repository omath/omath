package org.omath.core.javaObjects

import org.omath.bootstrap.JavaObjectExpression
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import org.apache.commons.codec.binary.Base64
import net.tqft.toolkit.Logging

object Serialize extends Logging {
  def apply(javaObject: Any): String = {
    val bos = new ByteArrayOutputStream()
    val out = new MagicObjectOutputStream(new ObjectOutputStream(bos))
    out.writeObject(javaObject)
    out.flush
    val bytes = bos.toByteArray()
    out.close
    bos.close
    Base64.encodeBase64String(bytes)
  }

}


