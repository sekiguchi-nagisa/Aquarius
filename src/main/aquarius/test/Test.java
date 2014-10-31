package aquarius.test;

import java.text.DecimalFormat;

import aquarius.matcher.Grammar;
import aquarius.matcher.ParserContext;
import aquarius.runtime.CommonStream;
import aquarius.runtime.Result;
import aquarius.runtime.Token;
import aquarius.util.TypeMatch;
import static aquarius.matcher.Expressions.*;

public class Test {
	public static void main(String[] args) {
		showMemory("before parsing");

		SampleGrammar g = new SampleGrammar();

		CommonStream input = new CommonStream("<sample>", "(((((((12345))))))");
		ParserContext context = new ParserContext(g, input);

		long start = System.currentTimeMillis();
		Result<Token> result = (Result<Token>) context.parse(g.Expr);
		long stop = System.currentTimeMillis();
		showMemory("after parsing");
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

	private final static DecimalFormat f = new DecimalFormat("#,###KB");
	private final static DecimalFormat f2 = new DecimalFormat("##,#");

	private static void showMemory(String message) {
		long total = Runtime.getRuntime().totalMemory() / 1024;
		long free = Runtime.getRuntime().freeMemory() / 1024;
		long max = Runtime.getRuntime().maxMemory() / 1024;
		long used = total - free;
		double ratio = used * 100 / (double) total;

		System.err.println(message);

		System.err.println("total memory: " + f.format(total));
		System.err.println("free  memory: " + f.format(free));
		System.err.println("used  memory: " + f.format(used) + " (" + f2.format(ratio) + "%)");
		System.err.println("max   memory: " + f.format(max));
		System.err.println();
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
		def(EOF, not(any()));

		def(__,
			zeroMore(ch(' ', '\t', '\n', '\r')).action((ctx, arg) -> null)
		);

		def(Expr,
			seq3(__, Add, __).action((ctx, arg) -> arg.get2())
		);

		def(Add,
			choice(
				$(seq5(Mul, __, str("+"), __, Add)),
				$(seq5(Mul, __, str("-"), __, Add)),
				Mul
			)
		);

		def(Mul,
			choice(
				$(seq5(Primary, __, str("*"), __, Mul)),
				$(seq5(Primary, __, str("/"), __, Mul)),
				Primary
			)
		);

		def(Primary,
			choice(
				$(seq5(str("("), __, Add, __, str(")"))),
				Num
			)
		);

		def(Num,
			choice(
				str("0"),
				$(seq3(opt(ch('-', '+')), ch()._r('1', '9'), zeroMore(ch()._r('0', '9'))))
			)
		);
	}
}