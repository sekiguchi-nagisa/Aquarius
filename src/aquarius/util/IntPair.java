package aquarius.util;

public class IntPair {
	private final int left;
	private final int right;

	public IntPair(int left, int right) {
		this.left = left;
		this.right = right;
	}

	public int getLeft() {
		return this.left;
	}

	public int getRight() {
		return this.right;
	}

	public String toString() {
		return (char) left + "..." + (char) right;
	}
}
