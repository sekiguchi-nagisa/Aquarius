package aquarius.test;

import aquarius.combinator.ParserContext;
import aquarius.combinator.expression.ParsingExpression;
import aquarius.combinator.expression.Rule;
import aquarius.runtime.CommonStream;
import aquarius.runtime.ParsedResult;
import static aquarius.combinator.expression.ParsingExpression.*;

public class Test {
	public static void main(String[] args) {
		CommonStream input = new CommonStream("<sample>", "(((((((12345))))))");
		ParserContext context = new ParserContext(input, Ex.values().length);

		long start = System.currentTimeMillis();
		ParsedResult result = Ex.Expr.parse(context);
		long stop = System.currentTimeMillis();
		System.out.println("parse time: " + (stop - start) + "ms");
		System.out.println(result);
	}
}

enum Ex implements Rule {
	EOF, __, hoge, Text, Expr, Add, Mul, Primary, Num,;
	
	static {
		EOF.pattern = not(any());

		__.pattern = choice(zeroMore(ch(' ', '\t', '\n', '\r')), EOF);

		hoge.pattern = 
			action(
				seq(hoge, __, hoge), 
				s -> s
			);

		Text.pattern = 
			capture(
				seq(
					ch('_')._$('a', 'z')._$('A', 'Z'),
					zeroMore(ch('_')._$('a', 'z')._$('A', 'Z')._$('0', '9'))
				)
			);

		Expr.pattern = seq(__, Add, __);

		Add.pattern = 
			capture(
				choice(
					seq(Mul, __, str("+"), __, Add),
					seq(Mul, __, str("-"), __, Add),
					Mul
				)
			);

		Mul.pattern = 
			choice(
				seq(Primary, __, str("*"), __, Mul),
				seq(Primary, __, str("/"), __, Mul),
				Primary
			);

		Primary.pattern = 
			choice(
				seq(str("("), __, Add, __, str(")")),
				Num
			);

		Num.pattern = 
			choice(
				str("0"),
				seq(opt(ch('-', '+')), ch()._$('1', '9'), zeroMore(ch()._$('0', '9')))
			);
	}

	protected ParsingExpression pattern;

	@Override
	public String getRuleName() {
		return this.name();
	}

	@Override
	public ParsingExpression getPattern() {
		return this.pattern;
	}

	@Override
	public int getRuleIndex() {
		return this.ordinal();
	}
}