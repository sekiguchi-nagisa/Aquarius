package aquarius.example;

import java.io.IOException;
import java.util.List;

import aquarius.matcher.Grammar;
import aquarius.matcher.ParserContext;
import aquarius.misc.Utils;
import aquarius.runtime.CommonStream;
import aquarius.runtime.Failure;
import aquarius.runtime.Token;
import static aquarius.matcher.Expressions.*;

public class Test {
	public static void main(String[] args) throws IOException {
		test1();
		test2(args);
	}

	private static void test1() {
		SampleGrammar g = new SampleGrammar();

		CommonStream input = new CommonStream("<sample>", "(((((((12345))))))");
		ParserContext context = new ParserContext(g, input);

		Utils.showMemory("before parsing");
		long start = System.currentTimeMillis();
		boolean result = context.parse(g.Expr);
		long stop = System.currentTimeMillis();
		Utils.showMemory("after parsing");
		System.out.println("parse time: " + (stop - start) + "ms");

		// print result
		System.err.println(result ? "sucess" : "failed");
	}

	private static void test2(String[] args) throws IOException {
		// json

		JSONGrammar jsonGrammar = new JSONGrammar();
		CommonStream input = new CommonStream(args[0]);
		ParserContext context = new ParserContext(jsonGrammar, input);

		Utils.showMemory("before parsing");
		long start = System.currentTimeMillis();
		boolean result = context.parse(jsonGrammar.json);
		long stop = System.currentTimeMillis();
		Utils.showMemory("after parsing");
		System.out.println("parse time: " + (stop - start) + "ms");

		// print result
		if(result) {
			System.out.println("success");
		} else {
			Failure f = context.popFailure();
			System.err.println(f.getMessage(input));
		}
	}
}

class SampleGrammar extends Grammar {
	public final Rule<Void> EOF = rule("EOF");
	public final Rule<List<Void>> __ = rule("__");
	public final Rule<Token> Expr = rule("Expr");
	public final Rule<Token> Add = rule("Add");
	public final Rule<Token> Mul = rule("Mul");
	public final Rule<Token> Primary = rule("Primary");
	public final Rule<Token> Num = rule("Num");

	public SampleGrammar() {
		def(EOF, not(ANY));

		def(__,
			zeroMore(ch(' ', '\t', '\n', '\r'))
		);

		def(Expr,
			seq(__, Add, __).action((ctx, a) -> a.get2())
		);

		def(Add,
			choice(
				$(Mul, __, str("+"), __, Add),
				$(Mul, __, str("-"), __, Add),
				Mul
			)
		);

		def(Mul,
			choice(
				$(Primary, __, str("*"), __, Mul),
				$(Primary, __, str("/"), __, Mul),
				Primary
			)
		);

		def(Primary,
			choice(
				$(str("("), __, Add, __, str(")")),
				Num
			)
		);

		def(Num,
			choice(
				$(str("0")),
				$(opt(ch('-', '+')), r('1', '9'), zeroMore(r('0', '9')))
			)
		);
	}
}
