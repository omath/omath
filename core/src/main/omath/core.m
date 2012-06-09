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
  
  (* And then allow just writing symbols for methods. *)
  (* Note that ToString isn't defined at this point, so you can't rely on this rule for a little while longer. *)
    
    JavaMethod[class_, symbol_Symbol] := JavaMethod[class, ToString[symbol]]

(* TODO ScalaObject[name_String] for obtaining Scala singletons *)    
(* TODO? GetExtensionInstance[class_String] for obtaining instances whose constructors require 'infrastructure', e.g. a kernel reference *)
