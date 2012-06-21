package org.omath.ui.webstart

import org.omath.ui.rest.Web

object WebStart extends App {
	Web.run
	java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost:8080/omath.html"));
}