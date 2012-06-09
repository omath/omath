package org.omath.bootstrap

class SingletonHelper(name: String) {
	def apply(): Any = {
	  Class.forName(name + "$").getDeclaredField("MODULE$").get(null)
	}
}