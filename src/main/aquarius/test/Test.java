package aquarius.test;

import java.text.DecimalFormat;

import aquarius.combinator.Grammar;
import aquarius.combinator.ParserContext;
import aquarius.combinator.expression.Rule;
import aquarius.runtime.CommonStream;
import aquarius.runtime.Result;
import aquarius.runtime.Token;
import static aquarius.combinator.expression.ParsingExpression.*;

public class Test {
	public static void main(String[] args) {
		showMemory("before parsing");

		SampleGrammar g = new SampleGrammar();

		CommonStream input = new CommonStream("<sample>", "(((((((12345))))))");
		ParserContext context = new ParserContext(input, g.getIndexSize());

		long start = System.currentTimeMillis();
		Result<Token> result = g.Expr.parse(context);
		long stop = System.currentTimeMillis();
		showMemory("after parsing");
		System.out.println("parse time: " + (stop - start) + "ms");
		System.out.println(result);
		
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
		this.EOF.setPattern(not(any()));

		this.__.setPattern(
			action(zeroMore(ch(' ', '\t', '\n', '\r')), arg -> Result.empty())
		);

		this.Expr.setPattern(
			action(seq3(__, Add, __), arg -> Result.of(arg.get2()))
		);

		this.Add.setPattern(
			choice(
				$(seq5(Mul, __, str("+"), __, Add)),
				$(seq5(Mul, __, str("-"), __, Add)),
				Mul
			)
		);

		this.Mul.setPattern(
			choice(
				$(seq5(Primary, __, str("*"), __, Mul)),
				$(seq5(Primary, __, str("/"), __, Mul)),
				Primary
			)
		);

		this.Primary.setPattern(
			choice(
				$(seq5(str("("), __, Add, __, str(")"))),
				Num
			)
		);

		this.Num.setPattern(
			choice(
				str("0"),
				$(seq3(opt(ch('-', '+')), ch()._r('1', '9'), zeroMore(ch()._r('0', '9'))))
			)
		);
	}
}