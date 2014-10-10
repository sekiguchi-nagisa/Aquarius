package aquarius.test;

import aquarius.combinator.Evaluator;
import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.expression.ParsingExpression;
import aquarius.combinator.expression.Rule;
import aquarius.runtime.BufferedStream;
import aquarius.runtime.ParsedResult;
import aquarius.runtime.memo.MapBasedMemoTableFactory;
import aquarius.runtime.memo.NullMemoTableFactory;
import static aquarius.combinator.expression.ParsingExpression.*;

public class Test {
	public static void main(String[] args) {
		Evaluator evaluator = new Evaluator(Ex.values());
		//BufferedStream input = new BufferedStream("<sample>", "hfreui35_d");
		//BufferedStream input = new BufferedStream("<sample>", "((((12 + 43 * (54 - 32 / 2)))))");
		BufferedStream input = new BufferedStream("<sample>", "(((((((12345))))))");
		evaluator.setInputStream(input);
//		evaluator.setMemoTableFactory(new NullMemoTableFactory());
//		evaluator.setMemoTableFactory(new MapBasedMemoTableFactory());
		//ParsedResult result = evaluator.parse(Ex.Text.getRuleIndex());
		long start = System.currentTimeMillis();
		ParsedResult result = evaluator.parse(Ex.Expr.getRuleIndex());
		long stop = System.currentTimeMillis();
		System.out.println("parse time: " + (stop - start) + "ms");
		System.out.println(result);
	}
}

enum Ex implements Rule {
	EOF {
		@Override public void init() { this.expr = 
			not(any());
		}
	},
	__ {
		@Override public void init() { this.expr = 
			choice(zeroMore(ch(' ', '\t', '\n', '\r')), EOF);
		}
	},
	hoge {
		@Override public void init() { this.expr = 
			action(
				seq(hoge, __, hoge), 
				s -> s);
		}
	},
	Text {
		@Override public void init() { this.expr = 
			capture(
				seq(
					ch('_')._$('a', 'z')._$('A', 'Z'),
					zeroMore(ch('_')._$('a', 'z')._$('A', 'Z')._$('0', '9'))));
		}
	},
	Expr {
		@Override public void init() { this.expr = 
			seq(__, Add, __);
		}
	},
	Add {
		@Override public void init() { this.expr = 
			capture(
				choice(
					seq(Mul, __, string("+"), __, Add),
					seq(Mul, __, string("-"), __, Add),
					Mul
				)
			);
		}
	},
	Mul {
		@Override public void init() { this.expr = 
			choice(
				seq(Primary, __, string("*"), __, Mul),
				seq(Primary, __, string("/"), __, Mul),
				Primary
			);
		}
	},
	Primary {
		@Override public void init() { this.expr = 
			choice(
				seq(string("("), __, Add, __, string(")")),
				Num
			);
		}
	},
	Num {
		@Override public void init() { this.expr = 
			choice(
				string("0"),
				seq(ch()._$('1', '9'), zeroMore(ch()._$('0', '9')))
			);
		}
	},
	;

	protected ParsingExpression expr;

	public abstract void init();

	@Override
	public String getRuleName() {
		return this.name();
	}

	@Override
	public ParsingExpression getPattern() {
		return this.expr;
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