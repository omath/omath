package org.omath.patterns

import org.omath._
import org.omath.kernel.Evaluation

private case class FullFormExpressionPattern(override val expression: FullFormExpression, headPattern: Pattern, argumentPattern: Pattern) extends ExpressionPattern {
  override lazy val pure = headPattern.pure && argumentPattern.pure
  override def extend(a: PartialBinding)(implicit evaluation: Evaluation) = {
    a match {
      case PartialBinding(b1, expressions, _) => {
        expressions match {
          case (h: FullFormExpression) +: t => {
              for (b2 <- headPattern.extend(b1)(h); b3 <- argumentPattern.extend(b2)(h.arguments: _*)) yield {
                PartialBinding(b3, t, Seq(h))
              }            
          }
        }
        
//        // TODO once there's head/tail extractor for Seq, clean this up.
//        if (expressions.isEmpty) {
//          Nil.iterator
//        } else {
//          val x = expressions.head
//          x match {
//            case x: FullFormExpression => {
//              for (b2 <- headPattern.extend(b1)(x.head); b3 <- argumentPattern.extend(b2)(x.arguments: _*)) yield {
//                PartialBinding(b3, expressions.tail, Seq(x))
//              }
//            }
//            case _ => Nil.iterator
//          }
//        }
      }
    }
  }
}
