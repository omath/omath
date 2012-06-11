"Evaluation"
	SetAttributes[HoldComplete, HoldAll];
	SetAttributes[HoldForm, HoldAll];
	SetAttributes[HoldPattern, HoldAll];
	SetAttributes[CompoundExpression, HoldAll]
	
	SetAttributes[Condition, HoldAll];
	SetAttributes[Pattern, HoldFirst]
	
	ReleaseHold[Hold[x_]] := x
	ReleaseHold[x_] := x
	Evaluate[x__] := x
