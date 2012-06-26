(* Depends on Scoping.m and FlowControl.m *)

(* Define Set again, so it returns the evaluated answer. *)
(lhs_ = rhs_) := With[{evaluated = rhs}, lhs := rhs; rhs]

SetAttributes[TagSet, HoldAll]
TagSet[s_Symbol, lhs_, rhs_] := With[{evaluated = rhs}, TagSetDelayed[s, lhs, evaluated]; evaluated]
SetAttributes[TagSetDelayed, HoldAll]
TagSetDelayed[s_Symbol, lhs_, rhs_] := ScalaObject["org.omath.core.assignments.TagSetDelayed"]["apply", Hold[s, lhs, rhs]]

EagerReflection /: (lhs_ := constructedAs:EagerReflection[ScalaObject[scalaObjectName_String][methodName_String, Hold[arguments___]]]) := ScalaObject["org.omath.core.assignments.EagerReflection"]["scalaObject", Hold[lhs, constructedAs, scalaObjectName, methodName, {arguments}]]