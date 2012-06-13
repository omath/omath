"Some basic string operations."
	StringJoin[strings___String] := ScalaFunction["{ strings: Seq[String] => strings.mkString }"][{strings}]
		CreateUnitTest[StringJoin, "should concatenate strings.", StringJoin["foo", "bar"] === "foobar"]
		CreateUnitTest[StringJoin, "with no arguments should give the empty string.", StringJoin[] === ""]
