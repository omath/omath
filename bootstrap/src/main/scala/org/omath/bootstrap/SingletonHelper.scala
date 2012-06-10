package org.omath.bootstrap

class SingletonHelper() {
	def apply(name: String): Any = {
	  Class.forName(name + "$").getDeclaredField("MODULE$").get(null)
	}
}