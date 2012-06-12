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

  class MagicObjectInputStream(delegate: ObjectInputStream) extends ObjectInputStream(delegate) {
    override protected def resolveClass(desc: ObjectStreamClass): Class[_] = {
      if (readBoolean) {
        val bytesLength = readInt
        val bytes = new Array[Byte](bytesLength)
        readFully(bytes)
        ByteClassLoader.hint(desc.getName, bytes)
        ByteClassLoader.loadClass(desc.getName)
      } else {
        super.resolveClass(desc)
      }
    }

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

object ByteClassLoader extends ClassLoader {
  private val store = scala.collection.mutable.Map[String, Array[Byte]]()

  def hint(name: String, bytes: Array[Byte]) = {
    store.put(name, bytes)
  }
  def classBytes(name: String) = store(name)
  
  override protected def findClass(name: String): Class[_] = {
	  store.get(name) match {
	    case None => null
	    case Some(bytes) => defineClass(name, bytes, 0, bytes.length);
	  }
  }
}