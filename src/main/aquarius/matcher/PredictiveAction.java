package aquarius.matcher;

@FunctionalInterface
public interface PredictiveAction<A> {
	public boolean invoke(A arg);
}
