"Inspecting the kernel state"
	SetAttributes[OwnValues, HoldFirst]
	OwnValues[s_Symbol] := ScalaObject["org.omath.core.kernel.OwnValues"]["apply", Hold[s]]
		(* For now, it is now possible to define OwnValues using ScalaFunction, because we can't hold arguments. *)
		(* Just as Function can have attributes, maybe later ScalaFunction can too. *)
		(* 
			OwnValues[s_Symbol] := ScalaFunction["{ (s: SymbolExpression, kernel: Kernel) => kernel.kernelState.ownValues(s) }"][s]
		*)
	SetAttributes[DownValues, HoldFirst]
	DownValues[s_Symbol] := ScalaObject["org.omath.core.kernel.DownValues"]["apply", Hold[s]]
	SetAttributes[UpValues, HoldFirst]
	UpValues[s_Symbol] := ScalaObject["org.omath.core.kernel.UpValues"]["apply", Hold[s]]
	SetAttributes[SubValues, HoldFirst]
	SubValues[s_Symbol] := ScalaObject["org.omath.core.kernel.SubValues"]["apply", Hold[s]]
		CreateUnitTest[OwnValues, "of $Version should be a List containing a single :> with rhs a String.", MatchQ[OwnValues[$Version], {_ :> _String}]]

	KernelReference[] := ScalaFunction["{ kernel: Kernel => kernel }"][]	