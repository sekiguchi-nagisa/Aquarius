package aquarius.misc;

public class Tuple3<A, B, C> extends Tuple2<A, B> {
	private final C c;

	public Tuple3(A a, B b, C c) {
		super(a, b);
		this.c = c;
	}

	public C get3() {
		return this.c;
	}

	@Override
	public String toString() {
		return stringify(this.get1(), this.get2(), this.get3());
	}
}
