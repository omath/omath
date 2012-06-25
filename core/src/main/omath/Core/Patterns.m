(* Depends on UnitTests.m *)
(* MatchQ was already defined in UnitTests.m *)

	CreateUnitTest[MatchQ, "should match optional arguments.", MatchQ[f[1], f[1, Optional[_, 2]]]]
	CreateUnitTest[MatchQ, "should match optional arguments (2).", MatchQ[f[1], f[1, x_:2, y_:3]]]
	
	PatternsComparableQ[pattern1_, pattern2_] := ScalaObject["org.omath.patterns.Pattern"]["tryCompare"[pattern1, pattern2]]["nonEmpty"[]]
	PatternsOrderedQ[{pattern1_, pattern2_}] := ScalaObject["org.omath.patterns.Pattern"]["lteq"[pattern1, pattern2]]
		CreateUnitTest[PatternsOrderedQ, "should compare Blanks with and without heads.", PatternsOrderedQ[{_h, _}]]