/*
 * Created on Sep 21, 2005
 */
package org.omath.parser.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.omath.parser.Syntax2FullFormParser;

import scala.util.Try;

public final class ParserCommandLine {

	public static void main(String[] args) throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String inputString;
		int lineNumber = 1;

		while (true) {
			System.out.print("(parser) In[" + lineNumber + "] := ");
			inputString = in.readLine();
			Try<String> output = Syntax2FullFormParser.apply(inputString);
			if (output.isFailure()) {
				System.err.println(/* output */);
			} else {
				String outputString = output.get();
				System.out.print("(parser) Out[" + lineNumber + "]:= ");
				System.out.println(outputString);
			}

			++lineNumber;

		}
	}
}
