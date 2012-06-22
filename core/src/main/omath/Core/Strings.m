"Some basic string operations."
    StringJoin[strings___String] := ScalaObject["org.omath.core.strings.StringJoin"]["apply"[{strings}]]
		CreateUnitTest[StringJoin, "should concatenate strings.", StringJoin["foo", "bar"] === "foobar"]
		CreateUnitTest[StringJoin, "with no arguments should give the empty string.", StringJoin[] === ""]
		
	ToString[expression_] := ScalaObject["org.omath.core.strings.ToString"]["apply"[expression]]
	    CreateUnitTest[ToString, "should convert symbols to strings.", ToString[x] === "x"]
