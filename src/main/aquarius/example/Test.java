package aquarius.example;

import java.io.IOException;
import java.util.List;

import aquarius.CommonStream;
import aquarius.Failure;
import aquarius.ParserContext;
import aquarius.Token;
import aquarius.misc.Utils;
import static aquarius.Expressions.*;

public class Test {
	public static void main(String[] args) throws IOException {
//		test1();
//		test2(args);
	}

//	private static void test1() {
//		SampleGrammar g = new SampleGrammar();
//
//		CommonStream input = new CommonStream("<sample>", "(((((((12345))))))");
//		ParserContext context = new ParserContext(g, input);
//
//		Utils.showMemory("before parsing");
//		long start = System.currentTimeMillis();
//		boolean result = context.parse(g.Expr);
//		long stop = System.currentTimeMillis();
//		Utils.showMemory("after parsing");
//		System.out.println("parse time: " + (stop - start) + "ms");
//
//		// print result
//		System.err.println(result ? "sucess" : "failed");
//	}
//
//	private static void test2(String[] args) throws IOException {
//		// json
//
//		JSONParser jsonGrammar = new JSONParser();
//		CommonStream input = new CommonStream(args[0]);
//		ParserContext context = new ParserContext(jsonGrammar, input);
//
//		Utils.showMemory("before parsing");
//		long start = System.currentTimeMillis();
//		boolean result = context.parse(jsonGrammar.json);
//		long stop = System.currentTimeMillis();
//		Utils.showMemory("after parsing");
//		System.out.println("parse time: " + (stop - start) + "ms");
//
//		// print result
//		if(result) {
//			System.out.println("success");
//		} else {
//			Failure f = context.popFailure();
//			System.err.println(f.getMessage(input));
//		}
//	}
}
