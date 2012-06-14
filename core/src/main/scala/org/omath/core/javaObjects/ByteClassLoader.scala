package org.omath.core.javaObjects

object ByteClassLoader extends ClassLoader {
  org.omath.bootstrap.ClassLoaders.registerClassLoader(this)

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