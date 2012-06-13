"Install SetAttributes, Attributes and the basic functionality of Set."
	"SetAttributes"
		SetAttributes[symbols_, attribute_Symbol] := SetAttributes[symbols, {attribute}]
		SetAttributes[symbol_Symbol, attributes_] := SetAttributes[{symbol}, attributes]
		SetAttributes[symbols:{__Symbol}, attributes:{__Symbol}] := ScalaObject["org.omath.core.attributes.SetAttributes"]["apply"[symbols, attributes]]
	
	"Some important attributes"
		SetAttributes[Hold, HoldAll]
		SetAttributes[RuleDelayed, HoldRest]
	
	"Attributes"
		Attributes[symbol_Symbol] := ScalaObject["org.omath.core.attributes.Attributes"]["apply"[symbol]]
		(*
		Attributes[symbol_Symbol] := ScalaFunction["{ (symbol: SymbolExpression, kernel: Kernel) => kernel.kernelState.attributes(symbol) }"][symbol]
		*)