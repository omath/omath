package org.omath.core.javaObjects

import java.io.ObjectInputStream
import java.io.ObjectStreamClass

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
