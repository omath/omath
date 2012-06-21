package org.omath.core.javaObjects

import java.io.ObjectOutputStream
import java.io.IOException
import net.tqft.toolkit.Logging
import org.omath.bootstrap.ClassLoaders

// from http://thread.gmane.org/gmane.comp.lang.scala.internals/4506
// TODO post a reply there; this is a better version?
class MagicObjectOutputStream(delegate: ObjectOutputStream) extends ObjectOutputStream(delegate) with Logging {
  lazy val abstractFileClassLoaderClass = ClassLoaders.lookupClass("scala.tools.nsc.interpreter.AbstractFileClassLoader")
  private def isReplClassLoader(cl: ClassLoader) = {
    if (cl == null || cl.getClass == null) false
    else if (cl.getClass.getName == "scala.tools.nsc.interpreter.AbstractFileClassLoader") true
    else abstractFileClassLoaderClass match {
      case Some(c) => c.isAssignableFrom(cl.getClass)
      case None => false
    }
  }
  @throws(classOf[IOException])
  override protected def annotateClass(cl: Class[_]): Unit = {
    if (isReplClassLoader(cl.getClassLoader)) {
      info("annotateClass(" + cl.getName + ") with bytes from REPL ClassLoader")
      writeBoolean(true)
      val cll = cl.getClassLoader
      val bytes = abstractFileClassLoaderClass.get.getMethod("classBytes", classOf[String]).invoke(cl.getClassLoader, cl.getName).asInstanceOf[Array[Byte]]
//      val bytes = cll.asInstanceOf[AbstractFileClassLoader].classBytes(cl.getName)
      info("Serializing " + cl.getName + " with size = " + bytes.length + " from ClassLoader " + cll)
      if (bytes.length == 0) throw new IOException("Could not find REPL class for: " + cl.getName)
      writeInt(bytes.length)
      write(bytes)
    } else if (cl.getClassLoader == ByteClassLoader) {
      writeBoolean(true)
      val bytes = ByteClassLoader.classBytes(cl.getName)
      info("Serializing " + cl.getName + " with size = " + bytes.length + " from ByteClassLoader")
      writeInt(bytes.length)
      write(bytes)
    } else {
      writeBoolean(false)
      super.annotateClass(cl)
    }
  }
}