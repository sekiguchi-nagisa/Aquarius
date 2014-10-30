package aquarius.matcher;

public class FailedActionException extends RuntimeException {
	private static final long serialVersionUID = -8936691216403555784L;

	public FailedActionException() {
		super();
	}

	public FailedActionException(String message) {
		super(message);
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;	// not create stack trace
	}
}
