"Packaging up __ and ___ arguments correctly makes this not worth the effort, I think."

"A helper for defining rules that delegate to Scala objects in the core."
	SetAttributes[DelegateToPackage, HoldRest]
	DelegateToPackage[package_String, pattern:(_Symbol[___])] := ScalaObject["org.omath.core.delegate.DelegateToPackage"]["apply", Hold[package, pattern]]
	SetAttributes[DelegateToCore, HoldRest]
	DelegateToCore[subpackage_String, pattern:(_Symbol[___])] := DelegateToPackage["org.omath.core." <> subpackage, pattern]
	(* Now instead of writing
	 *	ZZZ[x_Symbol, y:List[Integer]] := ScalaObject["org.omath.core.zzz.ZZZ"]["apply", Hold[x, y]]
	 * you can just write 
	 *	DelegateToCore["zzz", ZZZ[x_Symbol, y:List[Integer]]]
	 *)
