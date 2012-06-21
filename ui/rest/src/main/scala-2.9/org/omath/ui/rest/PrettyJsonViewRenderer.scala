package org.omath.ui.rest

import _root_.org.bowlerframework.view.json.BigDecimalSerializer
import _root_.org.bowlerframework.{ Response, Request }
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._
import _root_.org.bowlerframework.exception.HttpException
import net.liftweb.json.Formats
import _root_.org.bowlerframework.view.ViewRenderer
import net.liftweb.json.Printer

/**
 * JSON implementation of ViewRenderer - will take a Model or Models and render a JSON representation of said Model
 */
class PrettyJsonViewRenderer(jsonFormats: Formats = (net.liftweb.json.DefaultFormats + new BigDecimalSerializer)) extends ViewRenderer {
  implicit val formats = jsonFormats

  // TODO this doesn't actually do what I want:
  def onError(request: Request, response: Response, exception: Exception) = {
    exception match {
      case exception: HttpException => response.setStatus(exception.code)
      case _ => {
        response.setStatus(500)
      }
    }
    response.setContentType("text/plain")
    exception.printStackTrace(response.getWriter)
    exception.printStackTrace(System.err)
  }

  def renderView(request: Request, response: Response, models: Seq[Any]) = {
    def jsonpWrapper(content: => Unit) = {
      request.getStringParameter("jsonp") match {
        case Some(callback) => {
          response.getWriter.write(callback + "(")
          content
          response.getWriter.write(")")
        }
        case None => content
      }
    }

    response.setContentType("application/json")
    jsonpWrapper {
      if (models.size == 0) {
        response.setStatus(204)
      } else if (models.size == 1) {
        models.foreach(f => {
          Printer.pretty(render(decompose(f)), response.getWriter)
        })
      } else {
        var json: JValue = null
        models.foreach(f => {
          val alias = getModelAlias(f)
          val value = getValue(f)
          if (json == null) json = new JField(alias, value)
          else json = json ++ JField(alias, value)
        })
        Printer.pretty(render(json), response.getWriter)
      }
    }
  }

  /**
   * renders a no model view, in the case of JSON, this simply returns a HTTP 204 - No Content response.
   */
  def renderView(request: Request, response: Response) = {
    response.setContentType("application/json")
    response.setStatus(204)
  }

  private def getValue(any: Any): JValue = decompose(getModelValue(any))

}
