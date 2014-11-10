package aquarius.misc;

public class Tuple4<A, B, C, D> extends Tuple3<A, B, C> {
	private final D d;

	public Tuple4(A a, B b, C c, D d) {
		super(a, b, c);
		this.d = d;
	}

	public D get3() {
		return this.d;
	}

	@Override
	public String toString() {
		return stringify(this.get0(), 
			this.get1(), this.get2(), this.get3());
	}
}
