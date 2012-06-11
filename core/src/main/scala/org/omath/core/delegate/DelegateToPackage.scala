package org.omath.core.delegate

import org.omath.Expression
import org.omath.kernel.Kernel
import org.omath.FullFormExpression
import org.omath.SymbolExpression
import scala.collection.JavaConversions
import org.reflections.Reflections
import net.tqft.toolkit.Logging

object DelegateToPackage extends Logging {
	def apply(`package`: String, pattern: Expression)(implicit kernel: Kernel) = {
	  pattern match {
	    case FullFormExpression(symbol: SymbolExpression, arguments) => {
	      // lookup the class
	      val targetClass = Class.forName(`package` + "." + symbol.name)
	      
	      // identify all names in the pattern, in the right order!
	      def findNames(pattern: Expression): Seq[SymbolExpression] = {
	        pattern match {
	          case org.omath.symbols.Pattern(s: SymbolExpression, inner) => s +: findNames(inner)
	          case FullFormExpression(head, arguments) => findNames(head) ++ arguments.flatMap(findNames)
	          case _ => Seq.empty
	        }
	      }
	      
	      pattern :> org.omath.core.symbols.ScalaObject(targetClass.getName)("apply", org.omath.symbols.Hold(findNames(pattern):_*))
	    }
	  }
	}
}