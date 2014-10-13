package aquarius.util;

public class Tuple3<A, B, C> {
	private final A a;
	private final B b;
	private final C c;

	public Tuple3(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public A get1() {
		return this.a;
	}

	public B get2() {
		return this.b;
	}

	public C get3() {
		return this.c;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('(');
		sBuilder.append(this.a);
		sBuilder.append(", ");
		sBuilder.append(this.b);
		sBuilder.append(", ");
		sBuilder.append(this.c);
		sBuilder.append(')');

		return sBuilder.toString();
	}
}
