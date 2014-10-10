package aquarius.runtime;

public class Failure implements ParsedResult {
	private final String message;
	private final int currentPos;

	public Failure(int currentPos, String message) {
		this.currentPos = currentPos;
		this.message = message;
	}

	public Failure(Failure failure) {
		this.currentPos = failure.getFailurePos();
		this.message = "mismatch all of choice. longest matched: " + failure.getMessage();
	}

	public String getMessage() {
		return this.message;
	}

	public int getFailurePos() {
		return this.currentPos;
	}
	@Override
	public String toString() {
		return "failure at " + this.currentPos + ": " + this.message;
	}
}
