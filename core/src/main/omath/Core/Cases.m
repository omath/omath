(* TODO take a "Heads" -> _ option; probably best to sort out PatternOptions[] *)

Cases[expression_, pattern_, levelSpecification_:{1}, take_:Infinity] := ScalaObject["org.omath.core.cases.Cases"]["apply"[expression, pattern, levelSpecification, take, False]]
Cases[expression_, rule:(_Rule|_RuleDelayed), levelSpecification_:{1}, take_:Infinity] := ScalaObject["org.omath.core.cases.Cases"]["withRule"[expression, rule, levelSpecification, take, False]]

	CreateUnitTest[Cases, "should find integers in a List.", Cases[{1, "1", 2, "3"}, _Integer] === {1, 2}]
	CreateUnitTest[Cases, "should find integers at all levels.", Cases[{1, "1", {2, {"4", 3}}, "3"}, _Integer, Infinity] === {1, 2, 3}]