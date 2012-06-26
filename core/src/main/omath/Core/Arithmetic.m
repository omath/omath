Plus[x___] := ScalaObject["org.omath.core.arithmetic.Plus"]["apply"[{x}]]
SetAttributes[Plus, {Flat, Orderless, OneIdentity}]

(* Plus can't reach fixed points, because it bounces back and forth with ScalaObject.
   Have to provide a way to shortcut. UpValues first, then ...
(*
	CreateUnitTest[Plus, "should add positive integers.", 1 + 2 === 3]
	CreateUnitTest[Plus, "should add integers.", 1 - 2 === -1]
	CreateUnitTest[Plus, "should collect terms.", a + 2 b + 3 a === 4 a + 2b]
*)

Times[x___] := ScalaObject["org.omath.core.arithmetic.Times"]["apply"[{x}]]
SetAttributes[Times, {Flat, Orderless, OneIdentity}]
