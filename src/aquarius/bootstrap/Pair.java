package aquarius.bootstrap;

public class Pair <L, R> {
	private L left;
	private R right;

	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public void setLeft(L left) {
		this.left = left;
	}

	public void setRight(R right) {
		this.right = right;
	}

	public L getLeft() {
		return this.left;
	}

	public R getRight() {
		return this.right;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('(');
		sBuilder.append(this.left);
		sBuilder.append(',');
		sBuilder.append(this.right);
		return sBuilder.toString();
	}
}
