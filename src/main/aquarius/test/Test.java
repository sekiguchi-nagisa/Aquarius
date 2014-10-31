package aquarius.test;

import aquarius.matcher.Grammar;
import aquarius.matcher.ParserContext;
import aquarius.misc.TypeMatch;
import aquarius.misc.Utils;
import aquarius.runtime.CommonStream;
import aquarius.runtime.Result;
import aquarius.runtime.Token;
import static aquarius.matcher.Expressions.*;

public class Test {
	public static void main(String[] args) {
		Utils.showMemory("before parsing");

		SampleGrammar g = new SampleGrammar();

		CommonStream input = new CommonStream("<sample>", "(((((((12345))))))");
		ParserContext context = new ParserContext(g, input);

		long start = System.currentTimeMillis();
		Result<Token> result = (Result<Token>) context.parse(g.Expr);
		long stop = System.currentTimeMillis();
		Utils.showMemory("after parsing");
		System.out.println("parse time: " + (stop - start) + "ms");

		// print result
		if(result.isFailure()) {
			System.err.println(result);
		} else {
			TypeMatch.match(result.get())
				.when(Token.class, e -> System.out.println(e.getText(input)))
				.orElse(e -> System.out.println("undefined class: " + e.getClass()));
		}
	}
}

class SampleGrammar extends Grammar {
	public final Rule<Void> EOF = rule("EOF");
	public final Rule<Void> __ = rule("__");
	public final Rule<Token> Expr = rule("Expr");
	public final Rule<Token> Add = rule("Add");
	public final Rule<Token> Mul = rule("Mul");
	public final Rule<Token> Primary = rule("Primary");
	public final Rule<Token> Num = rule("Num");

	public SampleGrammar() {
		def(EOF, not(ANY));

		def(__,
			zeroMore(ch(' ', '\t', '\n', '\r')).action((ctx, arg) -> null)
		);

		def(Expr,
			seq3(__, Add, __).action((ctx, arg) -> arg.get2())
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
				str("0"),
				$(opt(ch('-', '+')), r('1', '9'), zeroMore(r('0', '9')))
			)
		);
	}
}