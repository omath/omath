package org.omath.core.javaObjects

import org.omath.bootstrap.JavaObjectExpression
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import org.apache.commons.codec.binary.Base64
import java.io.InputStream
import java.io.ObjectStreamClass

object Deserialize {
  def apply(base64: String): Any = {
    val bytes = Base64.decodeBase64(base64)
    val bos = new ByteArrayInputStream(bytes)
    //    val out = ClassLoaderObjectInputStream(this.getClass.getClassLoader, bos)
    val out = new MagicObjectInputStream(new ObjectInputStream(bos))
    out.readObject
  }

  // unused at the moment ...
  // from https://github.com/NetLogo/NetLogo/blob/master/src/main/org/nlogo/util/ClassLoaderObjectInputStream.scala
  case class ClassLoaderObjectInputStream(classLoader: ClassLoader,
    inputStream: InputStream) extends ObjectInputStream(inputStream) {
    override def resolveClass(objectStreamClass: ObjectStreamClass): Class[_] = {
      val clazz = Class.forName(objectStreamClass.getName, false, classLoader)
      if (clazz != null) clazz
      else super.resolveClass(objectStreamClass)
    }
  }
}

