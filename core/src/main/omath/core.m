(*************************************************************
 *
 * The kernel state is almost a tabula rasa.
 * The bootstrap phase has provided rules for
 *  - SetDelayed[x_, y_]
 *  - JavaClass[class_String]
 *  - JavaClass[object_JavaObject]
 *  - JavaNew[class_String, arguments_]
 *  - JavaMethod[class_String, method_String]
 *  - (method_JavaMethod)[object_JavaObject, arguments_]
 *  - (method_JavaMethod)[Null, arguments_]
 * with right hand sides which are 'opaque' bindable objects.
 *
 * We now set up everything on top of that.
 *
 *************************************************************)

$Version := "0.0.1"

(* We now make it slightly easier to invoke java methods. *)
  (* First, allow the usual syntax so we don't need to explicitly write JavaMethod. *)
  
    (object_JavaObject)[method_[arguments___]] := JavaMethod[JavaClass[object], method][object, {arguments}]
  
  (* Next an alternative syntax, which is often useful with head -> Hold. *)
   
    (object_JavaObject)[method_, head_[arguments___]] := JavaMethod[JavaClass[object], method][object, head[arguments]]
  
  (* TODO calling static methods *)
  (* TODO? get field values *)
  
  (* And then allow just writing symbols for methods. *)
  (* Note that ToString isn't defined at this point, so you can't rely on this rule for a little while longer. *)
    
    JavaMethod[class_, symbol_Symbol] := JavaMethod[class, ToString[symbol]]

  (* Obtaining a reference to a Scala singleton object *)
    ScalaObject[name_String] := JavaNew["org.omath.bootstrap.SingletonHelper", {}]["apply"[name]]

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
	
	"Set"
		SetAttributes[Set, HoldFirst]
		(lhs_ = rhs_) := (lhs := rhs)
		"Because Set is only HoldFirst, the right hand side gets evaluated before we defer to SetDelayed."

"Define a few bare minimums needed to set up unit testing,"
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
    	CreateUnitTest[Attributes, "should show attributes on a symbol.", Attributes[SetDelayed] === {HoldAll}]
    	CreateUnitTest[Append, "should add an element to the end of a list.", Append[{1,2,3}, 4] === {1,2,3,4}]

"Evaluation"
	SetAttributes[Condition, HoldAll];
	SetAttributes[HoldComplete, HoldAll];
	SetAttributes[HoldForm, HoldAll];
	SetAttributes[HoldPattern, HoldAll];
	SetAttributes[CompoundExpression, HoldAll]
	
	SetAttributes[Pattern, HoldFirst]
	
	ReleaseHold[Hold[x_]] := x
	ReleaseHold[x_] := x
	Evaluate[x__] := x


"Inspecting the kernel state"
	SetAttributes[OwnValues, HoldFirst]
	OwnValues[s_Symbol] := ScalaObject["org.omath.core.kernel.OwnValues"]["apply", Hold[s]]
	SetAttributes[DownValues, HoldFirst]
	DownValues[s_Symbol] := ScalaObject["org.omath.core.kernel.DownValues"]["apply", Hold[s]]
	SetAttributes[UpValues, HoldFirst]
	UpValues[s_Symbol] := ScalaObject["org.omath.core.kernel.UpValues"]["apply", Hold[s]]
	SetAttributes[SubValues, HoldFirst]
	SubValues[s_Symbol] := ScalaObject["org.omath.core.kernel.SubValues"]["apply", Hold[s]]
		CreateUnitTest[OwnValues, "of $Version should be a List containing a single :> with rhs a String.", MatchQ[OwnValues[$Version], {_ :> _String}]]
	
"Contexts"

"Flow Control"
	"CompoundExpression"
		SetAttributes[CompoundExpression, HoldAll]
		CompoundExpression[expressions___] := ScalaObject["org.omath.core.flowcontrol.CompoundExpression"]["apply", Hold[{expressions}]]
			CreateUnitTest[CompoundExpression, "should return the last result.",  (a;b) === b]
			CreateUnitTest[CompoundExpression, "should return null when there's a trailing semicolon.", (a;b;) === Null]
			
	"If"
		SetAttributes[If, HoldRest]
		If[True, x_] := x
		If[False, _] := Null
		If[True, x_, _] := x
		If[False, _, y_] := y
		If[True, x_, _, _] := x
		If[False, _, y_, _] := y
		If[_ , _, _, z_] := z
			CreateUnitTest[If, "should select the first branch when the condition is true.", If[True, a] === a]
			CreateUnitTest[If, "should select the second branch when the condition is false.", If[False, a, b] === b]
			CreateUnitTest[If, "should select the third branch when the condition is neither.", If[0, a, b, c] === c]
			"TODO: Better test for side effects too!"
	
	"While"
		SetAttributes[While, HoldAll]
		While[test_, expression___] := If[test === True, expression; While[test, expression], Null]

	
(* TODO --- below this line is broken stuff being merged from init.t *)
		Set[Part[s_Symbol, parts___], rhs_] := ScalaObject["org.omath.core.set.Set"]["setPart", Hold[s, {parts}, rhs]]
		Set[lhs_List, rhs_List] := ScalaObject["org.omath.core.set.Set"]["setList", Hold[lhs, rhs]]

"Updating line numbers with $Pre and $Post"
	$Line = 0
	$Pre  := Function[{expression}, In[++$Line]:=expression; expression, {HoldAll}]
	$Post := Function[{expression}, Out[$Line]:=expression; expression]


(*		CreateUnitTest[Function, "should respect attributes.", List @@ (Function[x, Hold[x], {HoldAll}] /@ Hold[!True]) === {Hold[!True]}]  *)
