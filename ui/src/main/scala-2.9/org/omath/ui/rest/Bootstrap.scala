package org.omath.ui.rest
import org.bowlerframework.view.scalate._
import org.bowlerframework.Request
import org.bowlerframework.BowlerConfigurator
import org.bowlerframework.view.RenderStrategy
import org.bowlerframework.view.JsonViewRenderer
import com.recursivity.commons.bean.TransformerRegistry

/**
 * This class acts as the starting point and bootstrap point for our application
 */
class Bootstrap {
  BowlerConfigurator.setRenderStrategy(new RenderStrategy {
    def resolveViewRenderer(request: Request) = new ModelViewRenderer
  })
    
  // I think we're ready to start and instantiate our Controller.
  val controller = new OmathController
}