package aquarius.util;

public class Tuple4<A, B, C, D> extends Tuple3<A, B, C> {
	private final D d;

	public Tuple4(A a, B b, C c, D d) {
		super(a, b, c);
		this.d = d;
	}

	public D get4() {
		return this.d;
	}

	@Override
	public String toString() {
		return stringify(this.get1(), 
			this.get2(), this.get3(), this.get4());
	}
}
