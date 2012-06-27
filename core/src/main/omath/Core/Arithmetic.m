Plus[x___] := EagerReflection[ScalaObject["org.omath.core.arithmetic.Plus"]["apply", Hold[{x}]]]
SetAttributes[Plus, {Flat, Orderless, OneIdentity}]

	CreateUnitTest[Plus, "should add positive integers.", 1 + 2 === 3]
	CreateUnitTest[Plus, "should add integers.", 1 - 2 === -1]
	CreateUnitTest[Plus, "should collect terms.", a + 2 b + 3 a === 4 a + 2 b]

Times[x___] := EagerReflection[ScalaObject["org.omath.core.arithmetic.Times"]["apply", Hold[{x}]]]
SetAttributes[Times, {Flat, Orderless, OneIdentity}]

Power[x_, 0] := 1
	CreateUnitTest[Power, "should cancel opposite powers." a^(-1) a === 1]