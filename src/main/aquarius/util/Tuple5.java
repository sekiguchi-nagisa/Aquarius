package aquarius.util;

public class Tuple5<A, B, C, D, E> {
	private final A a;
	private final B b;
	private final C c;
	private final D d;
	private final E e;

	public Tuple5(A a, B b, C c, D d, E e) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
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

	public E get5() {
		return this.e;
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
		sBuilder.append(", ");
		sBuilder.append(this.e);
		sBuilder.append(')');

		return sBuilder.toString();
	}
}
