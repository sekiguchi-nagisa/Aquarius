package aquarius.test;

import aquarius.combinator.Evaluator;
import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.expression.ParsingExpression;
import aquarius.combinator.expression.Rule;
import aquarius.runtime.CommonStream;
import aquarius.runtime.ParsedResult;
import aquarius.runtime.memo.MapBasedMemoTableFactory;
import static aquarius.combinator.expression.ParsingExpression.*;

public class Test {
	public static void main(String[] args) {
		Evaluator evaluator = new Evaluator(Ex.values());
		//BufferedStream input = new BufferedStream("<sample>", "hfreui35_d");
		//BufferedStream input = new BufferedStream("<sample>", "((((12 + 43 * (54 - 32 / 2)))))");
		CommonStream input = new CommonStream("<sample>", "(((((((12345))))))");
		evaluator.setInputStream(input);
//		evaluator.setMemoTableFactory(new NullMemoTableFactory());
		evaluator.setMemoTableFactory(new MapBasedMemoTableFactory());
		//ParsedResult result = evaluator.parse(Ex.Text.getRuleIndex());
		long start = System.currentTimeMillis();
		ParsedResult result = evaluator.parse(Ex.Expr.getRuleIndex());
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

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitRule(this);
	}
}