package org.omath

package object bootstrap {
  def ??? = throw new NoSuchMethodError

  // TODO allow passing a JavaClass as the 'class parameter
  val JavaMethodRule = {
    import org.omath.patterns._
    import org.omath.symbols
    import org.omath.bootstrap.{ symbols => bootstrapSymbols }
    ReplacementRule(
      bootstrapSymbols.JavaMethod(
        symbols.Pattern('class, symbols.Blank(symbols.String)),
        symbols.Pattern('method, symbols.Blank(symbols.String))),
      MethodReflection)
  }

  val JavaClassRule = {
    import org.omath.patterns._
    import org.omath.symbols
    import org.omath.bootstrap.{ symbols => bootstrapSymbols }
    ReplacementRule(
      bootstrapSymbols.JavaClass(
        symbols.Pattern('class, symbols.Blank(symbols.String))),
      ClassReflection)
  }

  // TODO conversions for arguments
  val JavaMethodInvocationRule = {
    import org.omath.patterns._
    import org.omath.symbols
    import org.omath.bootstrap.{ symbols => bootstrapSymbols }
    ReplacementRule(
      symbols.Pattern('method, bootstrapSymbols.JavaMethod)(
        symbols.Pattern('object, symbols.Blank(bootstrapSymbols.JavaObject)),
        symbols.Pattern('arguments, symbols.Blank()(symbols.BlankNullSequence()))),
      MethodInvocation)
  }

  // TODO conversions for arguments
  // TODO allow passing a JavaClass as the 'class parameter
  val JavaNewRule = {
    import org.omath.patterns._
    import org.omath.symbols
    import org.omath.bootstrap.{ symbols => bootstrapSymbols }
    ReplacementRule(
      bootstrapSymbols.JavaNew(
        symbols.Pattern('class, symbols.Blank(symbols.String)),
        symbols.Pattern('arguments, symbols.Blank()(symbols.BlankNullSequence()))),
      JavaNew)
  }

}