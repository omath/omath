ReplaceAll[expression_, rule_Rule] := ReplaceAll[expression, {rule}]
ReplaceAll[expression_, rule_RuleDelayed] := ReplaceAll[expression, {rule}]
ReplaceAll[expression_, rules:{(_Rule|_RuleDelayed)...}] := ScalaObject["org.omath.core.replacements.ReplaceAll"]["apply"[expression, rules]]
	CreateUnitTest[ReplaceAll, "should replace subexpressions.", {1} /. 1 -> 2 === {2}]