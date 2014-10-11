package aquarius.combinator;

import aquarius.runtime.ParsedResult;

@FunctionalInterface
public interface PredictiveAction {
	public boolean invoke(ParsedResult arg);
}
