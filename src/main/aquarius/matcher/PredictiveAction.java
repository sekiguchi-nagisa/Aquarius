package aquarius.matcher;

@FunctionalInterface
public interface PredictiveAction<A> {
	public boolean invoke(ParserContext context, A arg) throws Exception;
}
