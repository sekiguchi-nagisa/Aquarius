package aquarius.runtime;

import java.util.LinkedHashMap;

import aquarius.runtime.expression.ParsingExpression;

public class RuleSet extends LinkedHashMap<String, ParsingExpression> implements ParsedResult {
	private static final long serialVersionUID = -5917062271988355319L;
}
