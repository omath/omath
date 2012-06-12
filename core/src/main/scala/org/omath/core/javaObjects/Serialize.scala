package org.omath.core.javaObjects

import org.omath.bootstrap.JavaObjectExpression
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import org.apache.commons.codec.binary.Base64

object Serialize {
  def apply(javaObject: JavaObjectExpression[_]): String = {
    val bos = new ByteArrayOutputStream()
    val out = new ObjectOutputStream(bos)
    out.writeObject(javaObject.contents)
    val bytes = bos.toByteArray()
    out.close
    bos.close
    Base64.encodeBase64String(bytes)
  }
}