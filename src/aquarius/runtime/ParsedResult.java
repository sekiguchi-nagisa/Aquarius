package aquarius.runtime;

public interface ParsedResult {
	/**
	 * for and or not prediction
	 */
	public final static EmptyResult EMPTY_RESULT = new EmptyResult();

	/**
	 * for optional or zero more
	 */
	public final static NullResult NULL_RESULT = new NullResult();

	public final static class EmptyResult implements ParsedResult {
		private EmptyResult(){}
	}

	public final static class NullResult implements ParsedResult {
		private NullResult() {}
	}
}
