package org.omath.core.javaObjects

import org.omath.bootstrap.JavaObjectExpression
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import org.apache.commons.codec.binary.Base64
import scala.tools.nsc.interpreter.AbstractFileClassLoader
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectStreamClass
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

  // from http://thread.gmane.org/gmane.comp.lang.scala.internals/4506
  // TODO post a reply there; this is a better version?
  class MagicObjectOutputStream(delegate: ObjectOutputStream) extends ObjectOutputStream(delegate) {
    lazy val classLoaderClass = classOf[AbstractFileClassLoader]
    private def isReplClassLoader(cl: ClassLoader) = {
      if (cl == null || cl.getClass == null) false
      else if (cl.getClass.getName == "scala.tools.nsc.interpreter.AbstractFileClassLoader") true
      else classLoaderClass.isAssignableFrom(cl.getClass)
    }
    @throws(classOf[IOException])
    override protected def annotateClass(cl: Class[_]): Unit = {
      if (isReplClassLoader(cl.getClassLoader)) {
        info("annotateClass(" + cl.getName + ") with bytes from REPL ClassLoader")
        writeBoolean(true)
        val cll = cl.getClassLoader.asInstanceOf[AbstractFileClassLoader]
        val bytes = cll.classBytes(cl.getName)
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

}

