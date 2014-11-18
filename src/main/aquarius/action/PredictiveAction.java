package aquarius.action;

import aquarius.ParserContext;

@FunctionalInterface
public interface PredictiveAction {
	public boolean invoke(ParserContext context) throws Exception;
}
