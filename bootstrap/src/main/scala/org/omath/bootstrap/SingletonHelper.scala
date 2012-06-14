package org.omath.bootstrap

class SingletonHelper() {
	def apply(name: String): Any = {
	  ClassLoaders.lookupClass(name + "$") match {
	    case Some(clazz) => clazz.getDeclaredField("MODULE$").get(null)
	    case None => throw new ClassNotFoundException(name + "$")
	  }
	}
}