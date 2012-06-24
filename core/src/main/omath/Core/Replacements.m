RulesPattern = _Rule|_RuleDelayed|{(_Rule|_RuleDelayed)...}

ReplaceAll[expression_, rules:RulesPattern] := ScalaObject["org.omath.core.replacements.ReplaceAll"]["apply"[expression, rules]]
	CreateUnitTest[ReplaceAll, "should replace subexpressions.", ({1} /. 1 -> 2) === {2}]
	
Replace[expression_, rules:RulesPattern, levelSpecification_:{0}] := ScalaObject["org.omath.core.replacements.Replace"]["apply"[expression, rules, levelSpecification]]
	CreateUnitTest[Replace, "should replace the entire expression.", Replace[{1}, {1} -> {2}] === {2}]
	CreateUnitTest[Replace, "should not replace subexpressions.", Replace[{1}, 1 -> 2] === {1}]
	CreateUnitTest[Replace, "should replace subexpressions at specified levels.", Replace[{1, {1}}, 1 -> 2, {2}] === {1, {2}}]
	
ReplaceRepeated[expression_, rules:RulesPattern] := FixedPoint[ReplaceAll[#, rules]&, expression] 