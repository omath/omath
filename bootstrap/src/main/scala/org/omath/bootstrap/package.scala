package org.omath

package object bootstrap {
  def ??? = throw new NoSuchMethodError
  
  val JavaMethod = SymbolExpression("JavaMethod", "Bootstrap")

  
  val JavaMethodRule = {
    import org.omath.patterns._
    import org.omath.symbols.{ Blank, String }
    ReplacementRule(JavaMethod(Blank('class), Blank('method)), MethodReflection)
  }
}