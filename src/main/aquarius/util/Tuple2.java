package aquarius.util;

public class Tuple2<A, B> {
	private final A a;
	private final B b;

	public Tuple2(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A get1() {
		return this.a;
	}

	public B get2() {
		return this.b;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('(');
		sBuilder.append(this.a);
		sBuilder.append(", ");
		sBuilder.append(this.b);
		sBuilder.append(')');

		return sBuilder.toString();
	}
}
