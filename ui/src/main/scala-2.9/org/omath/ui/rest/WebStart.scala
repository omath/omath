package org.omath.ui.rest

object WebStart extends App {
	Web.run
	java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost:8080/omath.html"));
}