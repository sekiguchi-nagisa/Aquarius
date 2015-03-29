/*
 * Copyright (C) 2014-2015 Nagisa Sekiguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package aquarius.example;

import java.io.IOException;

import aquarius.CommonStream;
import aquarius.Failure;
import aquarius.ParsedResult;
import aquarius.ParserFactory;
import aquarius.Token;
import aquarius.misc.Utils;

public class Test {
	public static void main(String[] args) throws IOException {
		test1();
		test2(args);
	}

	private static void test1() {
		SampleParser parser = ParserFactory.createParser(SampleParser.class);
		CommonStream input = new CommonStream("<sample>", "(((((((12345))))))");

		Utils.showMemory("before parsing");
		long start = System.currentTimeMillis();
		ParsedResult<Token> result = parser.Expr().parse(input);
		long stop = System.currentTimeMillis();
		Utils.showMemory("after parsing");
		System.out.println("parse time: " + (stop - start) + "ms");

		// print result
		if(result.isSuccess()) {
			System.out.println("success");
		} else {
			Failure f = result.getFailure();
			System.err.println(f.getMessage(input));
		}
	}

	private static void test2(String[] args) throws IOException {
		JSONParser parser = ParserFactory.createParser(JSONParser.class);
		CommonStream input = new CommonStream(args[0]);

		Utils.showMemory("before parsing");
		long start = System.currentTimeMillis();
		ParsedResult<JSON> result = parser.json().parse(input);
		long stop = System.currentTimeMillis();
		Utils.showMemory("after parsing");
		System.out.println("parse time: " + (stop - start) + "ms");

		// print result
		if(result.isSuccess()) {
			System.out.println("success");
		} else {
			Failure f = result.getFailure();
			System.err.println(f.getMessage(input));
		}
	}
}
