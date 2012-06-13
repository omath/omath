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
	AsJavaObject[expression_] := ScalaObject["org.omath.core.javaObjects.AsJavaObject"]["apply"[expression]]
	Serialize[object_JavaObject] := ScalaObject["org.omath.core.javaObjects.Serialize"]["apply"[object]]
	Deserialize[base64_String] := ScalaObject["org.omath.core.javaObjects.Deserialize"]["apply"[base64]]
	ScalaEval[code_String] := ScalaObject["org.omath.core.eval.ScalaEval"]["apply"[code]]
	ScalaFunction[function_String][arguments___] := ScalaEval[function]["apply"[arguments]]
	   (* TODO need a cached version of ScalaEval, but this is waiting on pattern specificity! *)
	   	   
"IO"
	Get[path_String] := ScalaObject["org.omath.core.io.Get"]["apply"[path]]
	Print[text___String] := ScalaObject["org.omath.core.io.Print"]["apply"[{text}]]

Get["Core/Attributes.m"]
Get["Core/Contexts.m"]
Get["Core/UnitTest.m"]
Get["Core/Evaluation.m"]	
Get["Core/Kernel.m"]
Get["Core/FlowControl.m"]
Get["Core/Strings.m"]
Get["Core/Functional.m"]
	
(* To ensure that we properly test serializability of the kernel, we need to create a new class via ScalaEval and save a reference to it. *)
	square = ScalaEval["{ x: Int => x*x }"]
	
	
(* TODO --- below this line is broken stuff being merged from init.t *)
		Set[Part[s_Symbol, parts___], rhs_] := ScalaObject["org.omath.core.set.Set"]["setPart", Hold[s, {parts}, rhs]]
		Set[lhs_List, rhs_List] := ScalaObject["org.omath.core.set.Set"]["setList", Hold[lhs, rhs]]

"Updating line numbers with $Pre and $Post"
	$Line = 0
	$Pre  := Function[{expression}, In[++$Line]:=expression; expression, {HoldAll}]
	$Post := Function[{expression}, Out[$Line]:=expression; expression]


(*		CreateUnitTest[Function, "should respect attributes.", List @@ (Function[x, Hold[x], {HoldAll}] /@ Hold[!True]) === {Hold[!True]}]  *)
