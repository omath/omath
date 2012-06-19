package org.omath.core

import org.omath.Expression
import org.omath.SymbolExpression
import org.omath.kernel.Kernel
import scala.collection.JavaConversions
import org.omath.FullFormExpression
import org.omath.StringExpression
import net.tqft.toolkit.Logging
import org.omath.core.io.$Path
import org.omath.parser.SyntaxParser
import org.omath.core.eval.ScalaEval
import org.omath.core.io.Get
import org.omath.bootstrap.ClassLoaders

trait Core extends Kernel with Logging { kernel: SyntaxParser =>
  private[this] var beforeContexts = true

  override protected def symbolizer = { s: String =>
    def evaluateOwnValues(x: SymbolExpression): Option[Expression] = {
      val iterator = kernelState.ownValues(x).apply(x)(kernel.newEvaluation)
      if (iterator.hasNext) {
        Some(iterator.next)
      } else {
        None
      }
    }
    val contexts = evaluateOwnValues(symbols.$ContextPath) match {
      case Some(FullFormExpression(org.omath.symbols.List, contexts)) => {
        beforeContexts = false
        contexts
      }
      case _ => {
        if (!beforeContexts) {
          warn("""$ContextPath did not evaluate to a list of strings. (Using fallback value {"System`", "Global`"}.)""")
        }
        Seq(StringExpression("System`"), StringExpression("Global`"))
      }
    }

    (for (
      StringExpression(context) <- contexts;
      symbol <- kernelState.symbols(context);
      if symbol.name == s
    ) yield symbol).headOption match {
      case Some(found) => found
      case None => {
        evaluateOwnValues(symbols.$Context) match {
          case Some(StringExpression(context)) => SymbolExpression(s, context)
          case _ => {
            if (!beforeContexts) {
              warn("""$Context did not evaluate to a string. (Using fallback value "System`".)""")
            }
            SymbolExpression(s, "System")
          }
        }
      }
    }
  }

  
  
  {
    ClassLoaders.registerClassLoader(this.getClass.getClassLoader)
    org.omath.util.Scala29Compatibility.future { ScalaEval }
    Get("Core.m")(this)
  }
}

