package aquarius.util;

public class Tuple4<A, B, C, D> {
	private final A a;
	private final B b;
	private final C c;
	private final D d;

	public Tuple4(A a, B b, C c, D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
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

	public D get4() {
		return this.d;
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
		sBuilder.append(", ");
		sBuilder.append(this.d);
		sBuilder.append(')');

		return sBuilder.toString();
	}
}
