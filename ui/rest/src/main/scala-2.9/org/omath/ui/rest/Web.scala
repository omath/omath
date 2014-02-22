package org.omath.ui.rest

import org.jboss.netty.handler.codec.http.{ HttpRequest, HttpResponse }
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{ Http, Response }
import com.twitter.finagle.Service
import com.twitter.util.Future
import java.net.InetSocketAddress
import util.Properties
import java.net.URI
import org.jboss.netty.handler.codec.http.QueryStringDecoder
import java.net.URL
import scala.io.Source
import argonaut._
import Argonaut._

object Web {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    println("Starting on port:" + port)
    ServerBuilder()
      .codec(Http())
      .name("omath")
      .bindTo(new InetSocketAddress(port))
      .build(new ResolverService)
    println("Started omath.")

  }
}

class ResolverService extends Service[HttpRequest, HttpResponse] {

  def apply(req: HttpRequest): Future[HttpResponse] = {
    val response = Response()

    val parameters = new QueryStringDecoder(req.getUri()).getParameters
    import scala.collection.JavaConverters._
    val syntax = Option(parameters.get("syntax")).map(_.asScala.headOption).flatten.getOrElse("")
    val result = TungstenCore.evaluateSyntax(syntax).get.toContextualString(TungstenCore.newEvaluation)

    response.setStatusCode(200)

    response.setContentType("text/plain")
    response.contentString = result

    Future(response)
  }
}
