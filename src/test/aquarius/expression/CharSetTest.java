package aquarius.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static aquarius.Expressions.*;
import static aquarius.misc.Utf8Util.*;

public class CharSetTest extends TestBase<Void> {

	@Before
	public void prepare() {
		this.expr = ch('@', '$', toUtf8Code('ω'), 
				toUtf8Code('あ'),toUtf8Code(0x21c56)).r('a', 'd').r('3', '5');
		this.initContext("@");
	}

	@Test
	public void test() {
		// test 1
		boolean result = this.expr.parse(this.context);
		this.success(result, 1);
		assertNull(context.popValue());

		// test 2
		this.initContext("a");
		result = this.expr.parse(this.context);
		this.success(result, 1);
		assertNull(context.popValue());

		// test 3
		this.initContext("d");
		result = this.expr.parse(this.context);
		this.success(result, 1);
		assertNull(context.popValue());

		// test 4
		this.initContext("4");
		result = this.expr.parse(this.context);
		this.success(result, 1);
		assertNull(context.popValue());

		// test 5
		this.initContext("ω");
		result = this.expr.parse(this.context);
		this.success(result, 2);
		assertNull(context.popValue());

		// test 6
		this.initContext("あ");
		result = this.expr.parse(this.context);
		this.success(result, 3);
		assertNull(context.popValue());

		// test 7
		String s = new String(Character.toChars(0x21c56));
		this.initContext(s);
		result = this.expr.parse(this.context);
		this.success(result, 4);
		assertNull(context.popValue());

		// failure test
		this.initContext("E");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 0);

		this.initContext("");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 0);
	}
}
