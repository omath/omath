(* ScalaEval only works in plain vanilla environments. It's not suitable for the Core. *)

ScalaEval[code_String] := ScalaObject["org.omath.core.eval.ScalaEval"]["apply"[code]]
ScalaFunction[function_String][arguments___] := ScalaEval[function]["apply"[arguments]]

(* Test serialization... *)
scalaSquare = ScalaEval["{ x: Int => x*x }"]
Deserialize[Serialize[scalaSquare]]
