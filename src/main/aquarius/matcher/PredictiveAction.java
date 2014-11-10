package aquarius.matcher;

@FunctionalInterface
public interface PredictiveAction {
	public boolean invoke(ParserContext context) throws Exception;
}
