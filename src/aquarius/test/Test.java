package aquarius.test;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.expression.ParsingExpression;
import aquarius.combinator.expression.Rule;
import static aquarius.combinator.expression.ParsingExpression.*;

public class Test {
	public static void main(String[] args) {
		
	}
}

enum Ex implements Rule {
	WhiteSpace {
		@Override public void init() {
			this.expr = zeroMore(ch(' ', '\t', '\n', '\r'));
		}
	},
	Add {
		@Override public void init() {
			this.expr = action(
							seq(Add, WhiteSpace, Add), 
							s -> s[0]);
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