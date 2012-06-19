"Functional programming"
	"Apply"
		Apply[_, x_Symbol] := x
		Apply[_, x_String] := x
		Apply[_, x_Real] := x
		Apply[_, x_Integer] := x 
		Apply[f_, _[leaves___]] := f[leaves]
			CreateUnitTest[Apply, "should replace the head of a List.", f @@ {a,b} === f[a, b]]
			CreateUnitTest[Apply, "should leave literals alone.", f @@ 3 === 3]
		Apply[f_, x_, {1}] := f @@ #& /@ x
	
	"Map"
		Map[_, x_Symbol] := x
		Map[_, x_String] := x
		Map[_, x_Real] := x
		Map[_, x_Integer] := x
        Map[f_, head_[leaves___]] := head @@ ScalaObject["org.omath.core.functional.Map"]["apply"[f, {leaves}]]
			CreateUnitTest[Map, "should apply a function to each argument of an expression.", f /@ g[a,b] === g[f[a],f[b]]]
			CreateUnitTest[Map, "should leave literals alone.", f /@ 3 === 3]
