//package net.categoricaldata.server
//
//import org.mortbay.jetty.Connector
//import org.mortbay.jetty.Server
//import org.mortbay.jetty.webapp.WebAppContext
//import org.mortbay.jetty.nio._
//import scala.util.Properties
//
//object Web extends App {
//  val server = new Server
//  val scc = new SelectChannelConnector
//  scc.setPort(Properties.envOrElse("PORT", "8080").toInt)
//  server.setConnectors(Array(scc))
//
//  val context = new WebAppContext()
//  context.setServer(server)
//  context.setContextPath("/")
//  context.setWar("src/main/webapp")
//
//  server.addHandler(context)
//
//  server.start()
//}
