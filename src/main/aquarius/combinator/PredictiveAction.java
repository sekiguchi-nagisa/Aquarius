package aquarius.combinator;

@FunctionalInterface
public interface PredictiveAction<A> {
	public boolean invoke(A arg);
}
