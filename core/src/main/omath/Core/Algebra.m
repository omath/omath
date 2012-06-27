(* Depends Replacements.m and Arithmetic.m *)

Expand[x_] := x //. {
	Power[t_Plus, k_Integer] :> ScalaObject["org.omath.core.algebra.Expand"]["expandPower"[t, k]],
	Times[f___Plus] :> ScalaObject["org.omath.core.algebra.Expand"]["expandProduct"[{f}]]
}

	CreateUnitTest[Expand, "should expand powers of sums.", Expand[(a+b)^2] === a^2 + 2 a b + b^2]
	CreateUnitTest[Expand, "should expand powers of sums (2).", Expand[(a+a^(-1))^2] === 2 + a^(-2) + a^2]
	CreateUnitTest[Expand, "should expand products.", Expand[(a+b)(c+d)] === a c + b c + a d + b d]