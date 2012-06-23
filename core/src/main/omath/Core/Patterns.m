(* Depends on UnitTests.m *)
(* MatchQ was already defined in UnitTests.m *)

	CreateUnitTest[MatchQ, "should match optional arguments.", MatchQ[f[1], f[1, Optional[_, 2]]]]
	CreateUnitTest[MatchQ, "should match optional arguments (2).", MatchQ[f[1], f[1, x_:2, y_:3]]]
	