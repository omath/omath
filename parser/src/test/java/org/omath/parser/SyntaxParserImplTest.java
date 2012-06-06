package org.omath.parser;

import java.io.StringReader;

import junit.framework.TestCase;

import org.omath.parser.Node;
import org.omath.parser.ParseException;
import org.omath.parser.SyntaxParserImpl;

public class SyntaxParserImplTest extends TestCase {

	static final SyntaxParserImpl parser = new SyntaxParserImpl(new StringReader(""));
	
//	public static void main(String[] args) {
//		junit.swingui.TestRunner.run(SyntaxParserImplTest.class);
//	}


	public void testFullForm1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("f[a][b]");
		String result = node.toString();
		assertEquals("f[a][b]", result);	
	}

	public void testFullForm2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("f[a, b][c]");
		String result = node.toString();
		assertEquals("f[a, b][c]", result);	
	}

	public void testZeroInteger() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("-0");
		String result = node.toString();
		assertEquals("0", result);	
	}

	public void testZeroInteger1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("00");
		String result = node.toString();
		assertEquals("0", result);	
	}

	public void testZeroInteger2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("-00");
		String result = node.toString();
		assertEquals("0", result);	
	}

	public void testZeroInteger3() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("00137");
		String result = node.toString();
		assertEquals("137", result);	
	}

	public void testUnaryminus0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("-1");
		String result = node.toString();
		assertEquals("-1", result);	
	}

	public void testUnaryMinus1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("1+-1");
		String result = node.toString();
		assertEquals("Plus[1, -1]", result);	
	}

	public void testUnaryMinus2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("1+ -1");
		String result = node.toString();
		assertEquals("Plus[1, -1]", result);	
	}

	public void testUnaryMinus3() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("1-1");
		String result = node.toString();
		assertEquals("Plus[1, -1]", result);	
	}

	public void testUnaryMinus4() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("- - 2");
		String result = node.toString();
		assertEquals("Times[-1, -2]", result);	
	}

	public void testUnaryMinus5() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("1 - - 2");
		String result = node.toString();
		assertEquals("Plus[1, Times[-1, -2]]", result);	
	}

	public void testUnaryPlus0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("+0");
		String result = node.toString();
		assertEquals("Plus[0]", result);	
	}

	public void testUnaryPlus1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("+17");
		String result = node.toString();
		assertEquals("Plus[17]", result);	
	}

	public void testMinus0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a-b");
		String result = node.toString();
		assertEquals("Plus[a, Times[-1, b]]", result);	
	}

	public void testMinus1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a-3");
		String result = node.toString();
		assertEquals("Plus[a, -3]", result);	
	}

	public void testPlus() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("1+1");
		String result = node.toString();
		assertEquals("Plus[1, 1]", result);	
	}

	public void testPlusTimesPrecedence() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("1+2*3");
		String result = node.toString();
		assertEquals("Plus[1, Times[2, 3]]", result);	
	}

	public void testPower0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x^2");
		String result = node.toString();
		assertEquals("Power[x, 2]", result);	
	}

	public void testPower1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("(a+b)^(c+d)");
		String result = node.toString();
		assertEquals("Power[Plus[a, b], Plus[c, d]]", result);	
	}

	public void testPower2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("4^4");
		String result = node.toString();
		assertEquals("Power[4, 4]", result);	
	}

	public void testPart() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("h[a[b]]");
		String result = node.toString();
		assertEquals("h[a[b]]", result);	
	}

	public void testPlusAndMinus() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a+b-c+d");
		String result = node.toString();
		assertEquals("Plus[a, b, Times[-1, c], d]", result);	
	}

	public void testPureFunction0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("#+1&");
		String result = node.toString();
		assertEquals("Function[Plus[Slot[1], 1]]", result);	
	}

	public void testPureFunction1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("#+1&[1]");
		String result = node.toString();
		assertEquals("Function[Plus[Slot[1], 1]][1]", result);	
	}

	public void testPureFunction2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("#&2");
		String result = node.toString();
		assertEquals("Times[Function[Slot[1]], 2]", result);	
	}

	public void testSlotSequence() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("List[##]&");
		String result = node.toString();
		assertEquals("Function[List[SlotSequence[1]]]", result);	
	}

	public void testSlotPrecedence() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("#n");
		String result = node.toString();
		assertEquals("Times[Slot[1], n]", result);	
	}

	public void testSlotSlotSequencePrecedence() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("###");
		String result = node.toString();
		assertEquals("Times[SlotSequence[1], Slot[1]]", result);	
	}

	public void testTightSlot() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("#2[a]");
		String result = node.toString();
		assertEquals("Slot[2][a]", result);	
	}

	public void testSlotBinding0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("#2");
		String result = node.toString();
		assertEquals("Slot[2]", result);	
	}

	public void testSlotBinding1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("#-2");
		String result = node.toString();
		assertEquals("Plus[Slot[1], -2]", result);	
	}

	public void testOut() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("%");
		String result = node.toString();
		assertEquals("Out[]", result);	
	}

	public void testOutMultiple() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("%%%%%");
		String result = node.toString();
		assertEquals("Out[-5]", result);	
	}

	public void testOutSpecific0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("%17");
		String result = node.toString();
		assertEquals("Out[17]", result);	
	}

	public void testOutSpecific1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("%-3");
		String result = node.toString();
		assertEquals("Plus[Out[], -3]", result);	
	}

	public void testBlank0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("_");
		String result = node.toString();
		assertEquals("Blank[]", result);	
	}

	public void testBlank1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("_h");
		String result = node.toString();
		assertEquals("Blank[h]", result);	
	}

	public void testBlank2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("_h[t]");
		String result = node.toString();
		assertEquals("Blank[h][t]", result);	
	}

	public void testBlank3() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("_(h[t])");
		String result = node.toString();
		assertEquals("Times[Blank[], h[t]]", result);	
	}

	public void testBlank4() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("__");
		String result = node.toString();
		assertEquals("BlankSequence[]", result);	
	}

	public void testBlank5() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("___h");
		String result = node.toString();
		assertEquals("BlankNullSequence[h]", result);	
	}

	public void testBlank6() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("___");
		String result = node.toString();
		assertEquals("BlankNullSequence[]", result);	
	}

	public void testBlank7() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("___");
		String result = node.toString();
		assertEquals("BlankNullSequence[]", result);	
	}

	public void testBlank8() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("_.");
		String result = node.toString();
		assertEquals("Optional[Blank[]]", result);	
	}

	public void testBlank9() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("s_h");
		String result = node.toString();
		assertEquals("Pattern[s, Blank[h]]", result);	
	}

	public void testBlank10() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("s_h");
		String result = node.toString();
		assertEquals("Pattern[s, Blank[h]]", result);	
	}

	public void testBlank11() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("s__");
		String result = node.toString();
		assertEquals("Pattern[s, BlankSequence[]]", result);	
	}

	public void testBlank12() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("s__h");
		String result = node.toString();
		assertEquals("Pattern[s, BlankSequence[h]]", result);	
	}

	public void testBlank13() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("s___");
		String result = node.toString();
		assertEquals("Pattern[s, BlankNullSequence[]]", result);	
	}

	public void testBlank14() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("s___h");
		String result = node.toString();
		assertEquals("Pattern[s, BlankNullSequence[h]]", result);	
	}

	public void testBlank15() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("s_.");
		String result = node.toString();
		assertEquals("Optional[Pattern[s, Blank[]]]", result);	
	}

	public void testCompoundExpression0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a;b;c");
		String result = node.toString();
		assertEquals("CompoundExpression[a, b, c]", result);	
	}

	public void testCompoundExpression1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a;b;");
		String result = node.toString();
		assertEquals("CompoundExpression[a, b, Null]", result);	
	}

	public void testCompoundExpression2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a;;b");
		String result = node.toString();
		assertEquals("CompoundExpression[a, Null, b]", result);	
	}

	public void testTagSet() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a /: b = c");
		String result = node.toString();
		assertEquals("TagSet[a, b, c]", result);	
	}

	public void testTagSetDelayed() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a/:b:=c");
		String result = node.toString();
		assertEquals("TagSetDelayed[a, b, c]", result);	
	}

	public void testTagUnset() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a/:b=.");
		String result = node.toString();
		assertEquals("TagUnset[a, b]", result);	
	}

	public void testPatternTest0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a?b");
		String result = node.toString();
		assertEquals("PatternTest[a, b]", result);	
	}

	public void testPatternTest1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x_h?b");
		String result = node.toString();
		assertEquals("PatternTest[Pattern[x, Blank[h]], b]", result);	
	}

	public void testPatternTest2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x_h?(#>1&)");
		String result = node.toString();
		assertEquals("PatternTest[Pattern[x, Blank[h]], Function[Greater[Slot[1], 1]]]", result);	
	}

	public void testPatternTestBadSyntax() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x_h?(#>1)&");
		String result = node.toString();
		assertEquals("Function[PatternTest[Pattern[x, Blank[h]], Greater[Slot[1], 1]]]", result);	
	}

	public void testList() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("{a,b,c}");
		String result = node.toString();
		assertEquals("List[a, b, c]", result);	
	}

	public void testPart0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("{a,b,c}[[2]]");
		String result = node.toString();
		assertEquals("Part[List[a, b, c], 2]", result);	
	}

	public void testPart1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("L[[-1]]");
		String result = node.toString();
		assertEquals("Part[L, -1]", result);	
	}

	public void testPart2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("L[[{1,1}]]");
		String result = node.toString();
		assertEquals("Part[L, List[1, 1]]", result);	
	}

	public void testPart3() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("({a,b,c})[[2]]");
		String result = node.toString();
		assertEquals("Part[List[a, b, c], 2]", result);	
	}

	public void testComment() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a+(*foo*)b");
		String result = node.toString();
		assertEquals("Plus[a, b]", result);	
	}

	public void testInvisibleTimes0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a(-b)");
		String result = node.toString();
		assertEquals("Times[a, Times[-1, b]]", result);	
	}

	public void testInvisibleTimes1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a-1");
		String result = node.toString();
		assertEquals("Plus[a, -1]", result);	
	}

	public void testInvisibleTimes2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("1-a");
		String result = node.toString();
		assertEquals("Plus[1, Times[-1, a]]", result);	
	}

	public void testInvisibleTimes3() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("-a-b");
		String result = node.toString();
		assertEquals("Plus[Times[-1, a], Times[-1, b]]", result);	
	}

	public void testInvisibleTimes4() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a+-b");
		String result = node.toString();
		assertEquals("Plus[a, Times[-1, b]]", result);	
	}

	public void testInvisibleTimes5() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a[]b[]/@c");
		String result = node.toString();
		assertEquals("Times[a[], Map[b[], c]]", result);	
	}

	public void testInvisibleTimes6() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a[]b[]+c");
		String result = node.toString();
		assertEquals("Plus[Times[a[], b[]], c]", result);	
	}

	public void testInvisibleTimes7() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a-b/@c");
		String result = node.toString();
		assertEquals("Plus[a, Times[-1, Map[b, c]]]", result);	
	}

	public void testInequality0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a<b<=c==d");
		String result = node.toString();
		assertEquals("Inequality[a, Less, b, LessEqual, c, Equal, d]", result);	
	}

	public void testInequality1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a>c<d!=e");
		String result = node.toString();
		assertEquals("Inequality[a, Greater, c, Less, d, Unequal, e]", result);	
	}

	public void testPostApply() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a//b");
		String result = node.toString();
		assertEquals("b[a]", result);	
	}

	public void testFullFormPart() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a[b][[c]]");
		String result = node.toString();
		assertEquals("Part[a[b], c]", result);	
	}

	public void testDollarLineNumber() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("$LineNumber");
		String result = node.toString();
		assertEquals("$LineNumber", result);	
	}

	public void testTest1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a&a");
		String result = node.toString();
		assertEquals("Times[Function[a], a]", result);	
	}

	public void testTest2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a!&a");
		String result = node.toString();
		assertEquals("Times[Function[Factorial[a]], a]", result);	
	}

	public void testTest3() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a&!a");
		String result = node.toString();
		assertEquals("Times[Factorial[Function[a]], a]", result);	
	}

	public void testTest4() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a&+b");
		String result = node.toString();
		assertEquals("Plus[Function[a], b]", result);	
	}

	public void testTest5() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a!++b");
		String result = node.toString();
		assertEquals("Times[Increment[Factorial[a]], b]", result);	
	}

	public void testTest6() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("++-a&");
		String result = node.toString();
		assertEquals("Function[PreIncrement[Times[-1, a]]]", result);	
	}

	public void testTest7() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("++-a&!");
		String result = node.toString();
		assertEquals("Factorial[Function[PreIncrement[Times[-1, a]]]]", result);	
	}

	public void testTest8() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("++-a!&");
		String result = node.toString();
		assertEquals("Function[PreIncrement[Times[-1, Factorial[a]]]]", result);	
	}

	public void testTest9() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a=b=c/;d");
		String result = node.toString();
		assertEquals("Set[a, Set[b, Condition[c, d]]]", result);	
	}

	public void testTest10() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a=b=c/;d/;e");
		String result = node.toString();
		assertEquals("Set[a, Set[b, Condition[Condition[c, d], e]]]", result);	
	}

	public void testTest11() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a=b/;c");
		String result = node.toString();
		assertEquals("Set[a, Condition[b, c]]", result);	
	}

	public void testTest12() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a/;c=b");
		String result = node.toString();
		assertEquals("Set[Condition[a, c], b]", result);	
	}

	public void testTest30() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a=b+c/;d");
		String result = node.toString();
		assertEquals("Set[a, Condition[Plus[b, c], d]]", result);	
	}

	public void testTest31() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a~h~b");
		String result = node.toString();
		assertEquals("h[a, b]", result);	
	}

	public void testTest32() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a~h~b~k~c");
		String result = node.toString();
		assertEquals("k[h[a, b], c]", result);	
	}

	public void testTest33() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a=b;c/;d");
		String result = node.toString();
		assertEquals("CompoundExpression[Set[a, b], Condition[c, d]]", result);	
	}

	public void testTest34() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("f[x]/:b=c");
		String result = node.toString();
		assertEquals("TagSet[f[x], b, c]", result);	
	}

	public void testTest35() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a+b/:c=d");
		String result = node.toString();
		assertEquals("TagSet[Plus[a, b], c, d]", result);	
	}

	public void testTest36() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a++ ++b");
		String result = node.toString();
		assertEquals("Times[Increment[Increment[a]], b]", result);	
	}

	public void testTest37() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a++++b");
		String result = node.toString();
		assertEquals("Times[Increment[Increment[a]], b]", result);	
	}

	public void testTest38() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a:=b=c");
		String result = node.toString();
		assertEquals("SetDelayed[a, Set[b, c]]", result);	
	}

	public void testTest39() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a`b");
		String result = node.toString();
		assertEquals("a`b", result);	
	}

	public void testTest40() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a++!b");
		String result = node.toString();
		assertEquals("Times[Factorial[Increment[a]], b]", result);	
	}

	public void testTest41() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("Evaluate[f[x]]_");
		String result = node.toString();
		assertEquals("Times[Evaluate[f[x]], Blank[]]", result);	
	}

	public void testTest42() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("sieve[primes_List, tail_List] := sieve[Append[primes, tail[[1]]], DeleteCases[tail, _?(Mod[#, tail[[1]]] == 0 &)]]");
		String result = node.toString();
		assertEquals("SetDelayed[sieve[Pattern[primes, Blank[List]], Pattern[tail, Blank[List]]], sieve[Append[primes, Part[tail, 1]], DeleteCases[tail, PatternTest[Blank[], Function[Equal[Mod[Slot[1], Part[tail, 1]], 0]]]]]]", result);	
	}

	public void testTest43() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("n_Integer?(#!=0&)");
		String result = node.toString();
		assertEquals("PatternTest[Pattern[n, Blank[Integer]], Function[Unequal[Slot[1], 0]]]", result);	
	}

	public void testImaginaryI() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a+b \\[ImaginaryI]");
		String result = node.toString();
		assertEquals("Plus[a, Times[b, I]]", result);	
	}

	public void testPatternTestWithPrefix() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a?++b");
		String result = node.toString();
		assertEquals("PatternTest[a, PreIncrement[b]]", result);	
	}

	public void testPatternTestWithPostfix() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a++?b");
		String result = node.toString();
		assertEquals("PatternTest[Increment[a], b]", result);	
	}

	public void testRemoveInitialBacktick() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("`a");
		String result = node.toString();
		assertEquals("a", result);	
	}

	public void testReplaceNearDecimalPoint0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("4/. 4->5");
		String result = node.toString();
		assertEquals("ReplaceAll[4, Rule[4, 5]]", result);	
	}

	public void testReplaceNearDecimalPoint1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("4/.4->5");
		String result = node.toString();
		assertEquals("Rule[Times[4, Power[0.4, -1]], 5]", result);	
	}

	public void testCompoundExpressionNull0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a;b;//c");
		String result = node.toString();
		assertEquals("c[CompoundExpression[a, b, Null]]", result);	
	}

	public void testCompoundExpressionNull1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a;b//c");
		String result = node.toString();
		assertEquals("CompoundExpression[a, c[b]]", result);	
	}

	public void testAlternatives0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("A|B");
		String result = node.toString();
		assertEquals("Alternatives[A, B]", result);	
	}

	public void testAlternatives1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("A|B|C+D;|E");
		String result = node.toString();
		assertEquals("Alternatives[CompoundExpression[Alternatives[A, B, Plus[C, D]], Null], E]", result);	
	}

	public void testProductOfBlanks() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x_ y_");
		String result = node.toString();
		assertEquals("Times[Pattern[x, Blank[]], Pattern[y, Blank[]]]", result);	
	}

	public void testProductOfBlanksWithoutSpace() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x_y_");
		String result = node.toString();
		assertEquals("Times[Pattern[x, Blank[y]], Blank[]]", result);	
	}

	public void testTimesBlank0() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("_ a");
		String result = node.toString();
		assertEquals("Times[Blank[], a]", result);	
	}

	public void testTimesBlank1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x__ a__ y");
		String result = node.toString();
		assertEquals("Times[Pattern[x, BlankSequence[]], Pattern[a, BlankSequence[]], y]", result);	
	}

	public void testIncrementBlank() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a_++");
		String result = node.toString();
		assertEquals("Increment[Pattern[a, Blank[]]]", result);	
	}

	public void testPatternA() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x:a");
		String result = node.toString();
		assertEquals("Pattern[x, a]", result);	
	}

	public void testOptionalB() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x:a:b");
		String result = node.toString();
		assertEquals("Optional[Pattern[x, a], b]", result);	
	}

	public void testOptionalC() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x_a:b");
		String result = node.toString();
		assertEquals("Optional[Pattern[x, Blank[a]], b]", result);	
	}

	public void testPatternD() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x:_a");
		String result = node.toString();
		assertEquals("Pattern[x, Blank[a]]", result);	
	}

	public void testOptionalE() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x:_:a");
		String result = node.toString();
		assertEquals("Optional[Pattern[x, Blank[]], a]", result);	
	}

	public void testPatternF() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x:f[x_]");
		String result = node.toString();
		assertEquals("Pattern[x, f[Pattern[x, Blank[]]]]", result);	
	}

	public void testPatternG() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("v:x:y:z");
		String result = node.toString();
		assertEquals("Pattern[v, Optional[Pattern[x, y], z]]", result);	
	}

	public void testOptionalH() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("u:v:x:y:z");
		String result = node.toString();
		assertEquals("Optional[Pattern[u, v], Optional[Pattern[x, y], z]]", result);	
	}

	public void testPatternI() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x :a");
		String result = node.toString();
		assertEquals("Pattern[x, a]", result);	
	}

	public void testPatternJ() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x: a");
		String result = node.toString();
		assertEquals("Pattern[x, a]", result);	
	}

	public void testOptionalK() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x:a :h");
		String result = node.toString();
		assertEquals("Optional[Pattern[x, a], h]", result);	
	}

	public void testOptionalL() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x:_ :a");
		String result = node.toString();
		assertEquals("Optional[Pattern[x, Blank[]], a]", result);	
	}

	public void testPatternM() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("x:_ a");
		String result = node.toString();
		assertEquals("Pattern[x, Times[Blank[], a]]", result);	
	}

	public void testPatternN() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("r:Rational[_Integer,_Integer]");
		String result = node.toString();
		assertEquals("Pattern[r, Rational[Blank[Integer], Blank[Integer]]]", result);	
	}

	public void testPatternO() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a:r[_]?f");
		String result = node.toString();
		assertEquals("Pattern[a, PatternTest[r[Blank[]], f]]", result);	
	}

	public void testPatternP() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a:r[x][y]");
		String result = node.toString();
		assertEquals("Pattern[a, r[x][y]]", result);	
	}

	public void testPatternQ() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a:r[x][[1]]");
		String result = node.toString();
		assertEquals("Pattern[a, Part[r[x], 1]]", result);	
	}

	public void testPatternPrecedence1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a+b:c");
		String result = node.toString();
		assertEquals("Plus[a, Pattern[b, c]]", result);	
	}

	public void testPatternPrecedence2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a:c~~d");
		String result = node.toString();
		assertEquals("StringExpression[Pattern[a, c], d]", result);	
	}

	public void testPatternPrecedence3() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a:c<=d");
		String result = node.toString();
		assertEquals("Pattern[a, LessEqual[c, d]]", result);	
	}

	public void testPatternR() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a_b:(--c)");
		String result = node.toString();
		assertEquals("Optional[Pattern[a, Blank[b]], PreDecrement[c]]", result);	
	}

	public void testPatternS() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a_b:c+d");
		String result = node.toString();
		assertEquals("Optional[Pattern[a, Blank[b]], Plus[c, d]]", result);	
	}

	public void testAtPrecedence() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a@b@c");
		String result = node.toString();
		assertEquals("a[b[c]]", result);	
	}

	public void testSlashSlashPrecedence() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a//b//c");
		String result = node.toString();
		assertEquals("c[b[a]]", result);	
	}

	public void testPartA() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("#+3&[2][[3]]");
		String result = node.toString();
		assertEquals("Part[Function[Plus[Slot[1], 3]][2], 3]", result);	
	}

	public void testPartB() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("foo[[2,3]]");
		String result = node.toString();
		assertEquals("Part[foo, 2, 3]", result);	
	}

	public void testPartC() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("foo[[2]][[4]]");
		String result = node.toString();
		assertEquals("Part[Part[foo, 2], 4]", result);	
	}

	public void testPartD() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("foo[[2,3]][[4,1,2]]");
		String result = node.toString();
		assertEquals("Part[Part[foo, 2, 3], 4, 1, 2]", result);	
	}

	public void testSlashSlashPrecedence2() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a//b[3]//c[[2]]//d");
		String result = node.toString();
		assertEquals("d[Part[c, 2][b[3][a]]]", result);	
	}

	public void testPatternT() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a_:b");
		String result = node.toString();
		assertEquals("Optional[Pattern[a, Blank[]], b]", result);	
	}

	public void testPatternU() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a_?b:c");
		String result = node.toString();
		assertEquals("PatternTest[Pattern[a, Blank[]], Pattern[b, c]]", result);	
	}

	public void testPatternV() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a_h:c");
		String result = node.toString();
		assertEquals("Optional[Pattern[a, Blank[h]], c]", result);	
	}

	public void testPatternW() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a:b_.:c");
		String result = node.toString();
		assertEquals("Optional[Pattern[a, Optional[Pattern[b, Blank[]]]], c]", result);	
	}

	public void testPatternX() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a:b?c");
		String result = node.toString();
		assertEquals("Pattern[a, PatternTest[b, c]]", result);	
	}

	public void testPatternY() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a_?(#\\[Equal]5&)");
		String result = node.toString();
		assertEquals("PatternTest[Pattern[a, Blank[]], Function[Equal[Slot[1], 5]]]", result);	
	}

	public void testPatternZ() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("a:_?(#\\[Equal]5&):c");
		String result = node.toString();
		assertEquals("Optional[Pattern[a, PatternTest[Blank[], Function[Equal[Slot[1], 5]]]], c]", result);	
	}

	public void testPatternAA() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("_.");
		String result = node.toString();
		assertEquals("Optional[Blank[]]", result);	
	}

	public void testApply1() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("f @@@ g");
		String result = node.toString();
		assertEquals("Apply[f, g, {1}]", result);	
	}

	public void testRepeatedNull() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("(_Symbol = _)...");
		String result = node.toString();
		assertEquals("RepeatedNull[Set[Blank[Symbol], Blank[]]]", result);	
	}

	public void testMessageName() throws org.omath.parser.ParseException {
		Node node = SyntaxParserImpl.parseSyntaxString("expr::\"string\"");
		String result = node.toString();
		assertEquals("MessageName[expr, \"string\"]", result);	
	}

}
