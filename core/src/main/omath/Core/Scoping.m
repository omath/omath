"With"
	SetAttributes[With, HoldAll]
	With[variable:HoldPattern[_Symbol = _], body_] := With[{variable}, body]
	With[variables:{HoldPattern[_Symbol = _]...}, body_] := ScalaObject["org.omath.core.scoping.With"]["apply", Hold[variables, body]]
	
		CreateUnitTest[With, "should make replacements.", With[{a = 1}, {a, a}] === {1, 1}]