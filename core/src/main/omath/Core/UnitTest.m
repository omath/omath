"Define a few bare minimums needed to set up unit testing,"
	"Set"
		SetAttributes[Set, HoldFirst]
		(lhs_ = rhs_) := (lhs := rhs)
		"Because Set is only HoldFirst, the right hand side gets evaluated before we defer to SetDelayed."

    "Append"
		Append[head_[items___], item_] := head[items, item]
		
	"AppendTo"
		SetAttributes[AppendTo, HoldFirst]
		AppendTo[x_, item_] := x = Append[x, item]

	"SameQ"
		SameQ[values___] := ScalaObject["org.omath.core.equality.SameQ"]["apply"[{values}]]
	"MatchQ"
		MatchQ[expression_, pattern_] := ScalaObject["org.omath.core.patterns.MatchQ"]["apply"[expression, pattern]]

"Unit tests. You can run these via JUnit by testing 'CoreUnitTests'"
	$UnitTests = {}

	SetAttributes[CreateUnitTest, HoldAll]
	CreateUnitTest[symbol_Symbol, should_String, condition_] := AppendTo[$UnitTests, UnitTest[symbol, should, Hold[condition]]]
	
		"and make a trivial unit test, for verification."
		CreateUnitTest[True, "should be fixed by evaluation.", MatchQ[True, HoldPattern[True]]]

    	"We should catch up, and write some unit tests for stuff that happened earlier."
		CreateUnitTest[Get, "should slurp other files.", Get["GetTest.m"]; $GetTest]
    	CreateUnitTest[Attributes, "should show attributes on a symbol.", Attributes[SetDelayed] === {HoldAll}]
		CreateUnitTest[Context, "should return the context of a Symbol.", Context[Context] === "System`"]
    	CreateUnitTest[Append, "should add an element to the end of a list.", Append[{1,2,3}, 4] === {1,2,3,4}]