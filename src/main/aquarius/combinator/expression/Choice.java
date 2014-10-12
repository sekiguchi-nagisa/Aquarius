package aquarius.combinator.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Failure;
import aquarius.runtime.ParsedResult;

/**
* try to match first expression and if failed try the second one ...
* return matched result.
* -> expr1 / expr2 / ... / exprN
* @author skgchxngsxyz-opensuse
*
*/
public class Choice extends ListExpr {
	public Choice(ParsingExpression ...exprs) {
		super(exprs);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitChoice(this);
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		final int size = this.exprList.size();
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(" / ");
			}
			sBuilder.append(this.exprList.get(i));
		}
		return sBuilder.toString();
	}

	@Override
	public ParsedResult parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		// for error report
		List<Failure> failures = new ArrayList<>(this.getExprList().size());

		int pos = input.getPosition();
		for(ParsingExpression e : this.getExprList()) {
			ParsedResult result = e.parse(context);
			if(result instanceof Failure) {
				failures.add((Failure) result);
				input.setPosition(pos);	// roll back position
				continue;
			}
			return result;
		}
		Failure longestMatched = failures.get(0);
		for(Failure failure : failures) {
			if(failure.getFailurePos() > longestMatched.getFailurePos()) {
				longestMatched = failure;
			}
		}
		return longestMatched;	// longe
	}
}