/*
 * Created on Sep 21, 2005
 */
package org.omath.parser.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.omath.parser.Node;
import org.omath.parser.SyntaxParser;

public final class ParserCommandLine {
	
	
	public static void main(String[] args) throws IOException, org.omath.parser.ParseException {
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		String inputString;
		int lineNumber = 1;
		
		
		while(true) {
			System.out.print("(parser) In[" + lineNumber +"] := ");
			inputString = in.readLine();
			Node n = SyntaxParser.parseSyntaxString(inputString);
			System.out.print("(parser) Out[" + lineNumber +"]:= ");
			System.out.println(n.toString());
			
			++lineNumber;
		
		}
	}
}

