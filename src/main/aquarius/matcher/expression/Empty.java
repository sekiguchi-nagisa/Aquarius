package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;

/**
 * empty expression. return always Void value(actually null)
 * @author skgchxngsxyz-opensuse
 *
 */
public class Empty implements ParsingExpression<Void> {
	@Override
	public Result<Void> parse(ParserContext context) {
		return empty();
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitEmpty(this);
	}
}
