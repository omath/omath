"This is really stunning; embedding Groovy inside omath, without writing a single line of Groovy."

(* Unclear this should be in Core! *)
(* Also, this should be hidden in a package, to avoid executing AddToClassPath until it's actually needed. *)

AddToClassPath["http://repository.codehaus.org/org/codehaus/groovy/groovy-all/2.0.0-rc-2/groovy-all-2.0.0-rc-2.jar"]
GroovyEval[closure_String] := JavaClass["groovy.util.Eval"]["me"[closure]]
GroovyFunction[closure_String][arguments___] := GroovyEval[closure]["call"[arguments]]
groovyCube[x_Integer] := GroovyFunction["{int x -> x*x*x}"][x]
