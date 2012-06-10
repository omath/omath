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
    ScalaObject[name_String] := JavaNew["org.omath.bootstrap.SingletonHelper", {name}]["apply"[]]

(* TODO? GetExtensionInstance[class_String] for obtaining instances whose constructors require 'infrastructure', e.g. a kernel reference *)

(* TODO --- below this line is broken stuff being merged from init.t *)

Section["Set, SetDelayed, SetAttributes"]
"Install SetAttributes and Set."
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
		Set[lhs_, rhs_] := ScalaObject["org.omath.core.set.Set"]["set", Hold[lhs, rhs]]
		"Note how we hold arguments when delegating to Java."

		Set[Part[s_Symbol, parts___], rhs_] := ScalaObject["org.omath.core.set.Set"]["setPart", Hold[s, {parts}, rhs]]
		Set[lhs_List, rhs_List] := ScalaObject["org.omath.core.set.Set"]["setList", Hold[lhs, rhs]]

