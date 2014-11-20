package aquarius;

public class ParsedResult<R> {
	private final Object value;

	ParsedResult(Object value) {
		this.value = value;
	}

	public final boolean isSucess() {
		return !this.isFailure();
	}

	public boolean isFailure() {
		return this.value instanceof Failure;
	}

	/**
	 * 
	 * @return
	 * return null if parsing success
	 */
	public Failure getFailure() {
		return this.isFailure() ? (Failure) this.value : null;
	}

	/**
	 * 
	 * @return
	 * may be null if has no constructed value.
	 */
	@SuppressWarnings("unchecked")
	public R getValue() {
		return this.value != null && this.isSucess() ? (R) this.value : null;
	}
}
