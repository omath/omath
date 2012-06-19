package org.omath.ui.rest
import net.liftweb.json.Formats
import org.bowlerframework.view.ViewRenderer
import org.bowlerframework.Request
import org.bowlerframework.Response
import org.bowlerframework.exception.HttpException
import java.net.URL
import org.bowlerframework.http.BowlerHttpRequest
import org.bowlerframework.view.scalate.ScalateViewRenderer
import org.bowlerframework.view.ViewPath

class ModelViewRenderer(jsonFormats: Formats = net.liftweb.json.DefaultFormats) extends ViewRenderer {
  val prettyJsonViewRenderer = new PrettyJsonViewRenderer(jsonFormats)

  def onError(request: Request, response: Response, exception: Exception) = prettyJsonViewRenderer.onError(request, response, exception)

  def renderView(request: Request, response: Response, models: Seq[Any]) = {
    def completeURL = {
      val servletRequest = request.asInstanceOf[BowlerHttpRequest].getHttpServletRequest
      val queryString = servletRequest.getQueryString() match {
        case null => ""
        case "" => ""
        case q => "?" + q
      }
      servletRequest.getRequestURL.toString + queryString
    }
    lazy val serverURL = new URL(request.asInstanceOf[BowlerHttpRequest].getHttpServletRequest.getRequestURL.toString) match {
      case url => url.getProtocol() + "://" + url.getHost() + ((url.getProtocol(), url.getPort()) match {
        case ("http", p) if p == 80 => ""
        case ("https", p) if p == 443 => ""
        case (_, -1) => ""
        case (_, p) => ":" + p.toString
      })
    }

    val renderJSON = (request.getHeader("accept") match {
      case None => false
      case Some(accept) => {
        val lower = accept.toLowerCase
        lower.contains("text/javascript") || lower.contains("application/javascript") || lower.contains("text/json") || lower.contains("application/json")
      }
    }) || request.getStringParameter("jsonp").nonEmpty

    val locationString = request.getPath + (request.getParameterMap match {
      case m if m.isEmpty => ""
      case m => "?" + m.collect({case (name, value: String) => name + "=" + value}).mkString("&")
    })
    response.addHeader("Location", locationString)
    
    // FIXME hardwired JSON
    if (true || renderJSON) {
      prettyJsonViewRenderer.renderView(request, response, models)
    } else {
      val viewPath: ViewPath = "/omath/show"
      request.setMappedPath(viewPath.path)
      request.setMethod(viewPath.method)

      new ScalateViewRenderer().renderView(request, response, models)
    }

  }
}