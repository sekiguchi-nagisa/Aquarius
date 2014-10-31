package aquarius.misc;

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

	protected static String stringify(Object... values) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('(');
		for(int i = 0; i < values.length; i++) {
			if(i > 0) {
				sBuilder.append(", ");
			}
			sBuilder.append(values[i]);
		}
		sBuilder.append(')');
		return sBuilder.toString();
	}

	@Override
	public String toString() {
		return stringify(this.get1(), this.get2());
	}
}
