"Flow Control"
	"CompoundExpression"
		SetAttributes[CompoundExpression, HoldAll]
		CompoundExpression[expressions___] := ScalaObject["org.omath.core.flowcontrol.CompoundExpression"]["apply", Hold[{expressions}]]
			CreateUnitTest[CompoundExpression, "should return the last result.",  (a;b) === b]
			CreateUnitTest[CompoundExpression, "should return Null when there's a trailing semicolon.", (a;b;) === Null]
			
	"If"
		SetAttributes[If, HoldRest]
		If[True, x_] := x
		If[False, _] := Null
		If[True, x_, _] := x
		If[False, _, y_] := y
		If[True, x_, _, _] := x
		If[False, _, y_, _] := y
		If[_ , _, _, z_] := z
			CreateUnitTest[If, "should select the first branch when the condition is true.", If[True, a] === a]
			CreateUnitTest[If, "should select the second branch when the condition is false.", If[False, a, b] === b]
			CreateUnitTest[If, "should select the third branch when the condition is neither.", If[0, a, b, c] === c]
			"TODO: Better test for side effects too!"
	
	"While"
		SetAttributes[While, HoldAll]
		While[test_, expression___] := If[test === True, expression; While[test, expression], Null]
