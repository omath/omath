package org.omath.ui.rest

import org.mortbay.jetty.Connector
import org.mortbay.jetty.Server
import org.mortbay.jetty.webapp.WebAppContext
import org.mortbay.jetty.nio._
import scala.util.Properties
import java.io.File

object Web extends App {
  def run {
    val server = new Server
    val scc = new SelectChannelConnector
    scc.setPort(Properties.envOrElse("PORT", "8080").toInt)
    server.setConnectors(Array(scc))

    val context = new WebAppContext()
    context.setServer(server)
    context.setContextPath("/")

    // we might be run from various locations, so first work out where the webapp resides...
    Seq("src/main/webapp/", "ui/src/main/webapp").find(p => new File(p).exists) match {
      case Some(warLocation) => context.setWar(warLocation)
      case None => {
        context.setResourceBase(Web.getClass.getClassLoader.getResource("webapp").toExternalForm())
      }
    }

    server.addHandler(context)

    server.start()
  }

  run
}
