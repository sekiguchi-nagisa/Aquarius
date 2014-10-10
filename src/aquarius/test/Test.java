package aquarius.test;

import aquarius.combinator.Evaluator;
import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.expression.ParsingExpression;
import aquarius.combinator.expression.Rule;
import aquarius.runtime.BufferedStream;
import aquarius.runtime.ParsedResult;
import static aquarius.combinator.expression.ParsingExpression.*;

public class Test {
	public static void main(String[] args) {
		Evaluator evaluator = new Evaluator(Ex.values());
		BufferedStream input = new BufferedStream("<sample>", "hfreui35_d");
		evaluator.setInputStream(input);
		ParsedResult result = evaluator.parse(Ex.Text.getRuleIndex());
		System.out.println(result);
	}
}

enum Ex implements Rule {
	EOF {
		@Override public void init() {
			this.expr = not(any());
		}
	},
	WhiteSpace {
		@Override public void init() {
			this.expr = zeroMore(ch(' ', '\t', '\n', '\r'));
		}
	},
	Add {
		@Override public void init() {
			this.expr = action(
							seq(Add, WhiteSpace, Add), 
							s -> s);
		}
	},
	Text {
		@Override public void init() {
			this.expr = 
					capture(
						seq(
							ch('_')._$('a', 'z')._$('A', 'Z'),
							zeroMore(ch('_')._$('a', 'z')._$('A', 'Z')._$('0', '9'))));
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

	public int getRuleIndex() {
		return this.ordinal();
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitRule(this);
	}
}