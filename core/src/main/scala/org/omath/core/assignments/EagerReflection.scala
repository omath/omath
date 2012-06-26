package org.omath.core.assignments

import org.omath.Expression
import org.omath.SymbolExpression
import org.omath.StringExpression
import org.omath.SymbolExpression
import org.omath.symbols
import org.omath.util.Scala29Compatibility.???
import org.omath.kernel.Evaluation
import org.omath.bootstrap.JavaObjectExpression
import org.omath.bootstrap.JavaMethodExpression
import org.omath.bootstrap.MethodInvocationBindable
import org.omath.bootstrap.JavaMethodBindable
import org.omath.bootstrap.SingletonHelper
import org.omath.FullFormExpression

case class EagerReflectionBindable(constructedAs: FullFormExpression, javaObject: JavaObjectExpression[_], method: JavaMethodExpression, methodArguments: Seq[Expression]) extends FullFormExpression {
  override def head = constructedAs.head
  override def arguments = constructedAs.arguments
  override def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression] = ???
  override def activeBind(binding: Map[SymbolExpression, Expression])(implicit evaluation: Evaluation): Expression = {
    val boundArguments = methodArguments.map(_.bind(binding))

    val passedBinding = Map(
        SymbolExpression('object) -> javaObject,
        SymbolExpression('method) -> method,
        SymbolExpression('arguments) -> symbols.Sequence(boundArguments:_*))
    
    MethodInvocationBindable.activeBind(passedBinding)
  }
}

object EagerReflection {
  def scalaObject(constructedAs: FullFormExpression, scalaObjectName: String, methodName: String, arguments: Seq[Expression]) = {

    val javaObject = JavaObjectExpression(SingletonHelper(scalaObjectName))
    val method = JavaMethodBindable.bind(Map(
      SymbolExpression('class) -> JavaObjectExpression(javaObject.contents.getClass),
      SymbolExpression('method) -> StringExpression(methodName)))

    EagerReflectionBindable(constructedAs, javaObject, method, arguments)
  }
}