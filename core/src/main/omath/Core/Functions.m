SetAttributes[Function, HoldAll]
Function[body_] := Function[{}, body]
Function[variables:{___Symbol}, body_][arguments___] := ScalaObject["org.omath.core.functions.Function"]["apply", Hold[variables, body, {arguments}]]
	CreateUnitTest[Function, "should substitute an argument into the identity function.", #&[1] === 1]