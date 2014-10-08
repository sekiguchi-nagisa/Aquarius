package aquarius.runtime;

@FunctionalInterface
public interface PredictiveAction {
	public boolean invoke(ParsedResult... args);
}
