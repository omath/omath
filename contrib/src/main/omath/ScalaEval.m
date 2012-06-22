(* ScalaEval only works in plain vanilla environments. It's not suitable for the Core. *)

(* FIXME need to add some things to the classpath now! *)
AddToClassPath["... ScalaEval itself ..."]
AddToClassPath["... the Scala compiler ..."]

ScalaEval[code_String] := ScalaObject["org.omath.core.eval.ScalaEval"]["apply"[code]]
ScalaFunction[function_String][arguments___] := ScalaEval[function]["apply"[arguments]]