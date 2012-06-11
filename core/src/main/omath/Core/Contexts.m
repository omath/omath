"Contexts"
	SetAttributes[Context, HoldAll]
	Context[s_Symbol] := AsJavaObject[s]["context"[]]
	
	$Context = "System`"
	$ContextPath = {"System`", "Global`"}
