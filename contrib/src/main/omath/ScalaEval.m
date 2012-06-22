(* ScalaEval only works in plain vanilla environments. It's not suitable for the Core. *)

(* TODO automatically insert the version numbers in these URLs. *)
AddToClassPath["http://tqft.net/releases/org/omath/omath-contrib_2.9.2/0.0.1-SNAPSHOT/omath-contrib_2.9.2-0.0.1-SNAPSHOT.jar"]
AddToClassPath["https://oss.sonatype.org/content/repositories/releases/org/scala-lang/scala-compiler/2.9.2/scala-compiler-2.9.2.pom"]

ScalaEval[code_String] := ScalaObject["org.omath.core.eval.ScalaEval"]["apply"[code]]
ScalaFunction[function_String][arguments___] := ScalaEval[function]["apply"[arguments]]