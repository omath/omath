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
import org.omath.patterns.Pattern

case class EagerReflectionBindable(constructedAs: FullFormExpression, scalaObjectName: String, methodName: String, methodArguments: Seq[Expression]) extends FullFormExpression {
  
  @scala.transient
  private lazy val javaObject: JavaObjectExpression[_] = JavaObjectExpression(SingletonHelper(scalaObjectName))
  @scala.transient
  private lazy val method: JavaMethodExpression = JavaMethodBindable.bind(Map(
      SymbolExpression('class) -> JavaObjectExpression(javaObject.contents.getClass),
      SymbolExpression('method) -> StringExpression(methodName)))
  
  override def head = constructedAs.head
  override def arguments = constructedAs.arguments
  override def bindOption(binding: Map[SymbolExpression, Expression]): Option[Expression] = ???
  override def activeBind(binding: Map[SymbolExpression, Expression])(implicit evaluation: Evaluation): Expression = {
    val boundArguments = methodArguments.map(_.bind(binding))

    val passedBinding = Map(
      SymbolExpression('object) -> javaObject,
      SymbolExpression('method) -> method,
      SymbolExpression('arguments) -> symbols.List(boundArguments: _*))

    MethodInvocationBindable.activeBind(passedBinding)
  }
}

object EagerReflection {
  def scalaObject(lhs: Pattern, constructedAs: FullFormExpression, scalaObjectName: String, methodName: String, arguments: Seq[Expression])(implicit evaluation: Evaluation) = {
    implicit val attributes = evaluation.kernel.kernelState.attributes _
    (lhs.evaluateArguments :> EagerReflectionBindable(constructedAs, scalaObjectName, methodName, arguments)).install(evaluation.kernel.kernelState)
    symbols.Null
  }
}