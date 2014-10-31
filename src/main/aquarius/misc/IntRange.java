package aquarius.misc;

public class IntRange {
	private final int start;	// inclusive
	private final int stop;	// inclusive

	public IntRange(int start, int stop) {
		this.start = start;
		this.stop = stop;
	}

	public int getStart() {
		return this.start;
	}

	public int getStop() {
		return this.stop;
	}

	public boolean withinRange(int value) {
		return value >= this.start && value <= this.stop;
	}

	public String toString() {
		return (char) this.start + "..." + (char) this.stop;
	}
}
