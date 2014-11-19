package aquarius.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static aquarius.Expressions.*;

public class AnyTest extends TestBase<Void> {

	@Before
	public void prepare() {
		this.expr = ANY;
		this.initContext("@");
	}

	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		this.success(result, 1);
		assertNull(context.popValue());

		// test2
		this.initContext("ω");
		result = this.expr.parse(this.context);
		this.success(result, 2);
		assertNull(context.popValue());

		// test3
		this.initContext("あ");
		result = this.expr.parse(this.context);
		this.success(result, 3);
		assertNull(context.popValue());

		// test4
		this.initContext(new String(Character.toChars(0x21c56)));
		result = this.expr.parse(this.context);
		this.success(result, 4);
		assertNull(context.popValue());

		// failure test
		this.initContext("");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 0);
	}
}
