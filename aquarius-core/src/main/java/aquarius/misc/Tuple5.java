package aquarius.misc;

public class Tuple5<A, B, C, D, E> extends Tuple4<A, B, C, D> {
	private final E e;

	public Tuple5(A a, B b, C c, D d, E e) {
		super(a, b, c, d);
		this.e = e;
	}

	public E get4() {
		return this.e;
	}

	@Override
	public String toString() {
		return stringify(this.get0(), 
			this.get1(), this.get2(), this.get3(), this.get4());
	}
}