(* Depends Replacements.m and Arithmetic.m *)

Expand[x_] := x //. {
	Power[t_Plus, k_Integer] :> ScalaObject["org.omath.core.algebra.Expand"]["expandPower"[t, k]],
	Times[f___Plus] :> ScalaObject["org.omath.core.algebra.Expand"]["expandProduct"[{f}]]
}
