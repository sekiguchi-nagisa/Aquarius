package aquarius.combinator;

import static aquarius.combinator.expression.ParsingExpression.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;

public class OptionalTest extends TestBase<Optional<Token>> {

	@Before
	public void prepare() {
		this.expr = opt(str("hello"));
		this.initContext("g");
	}

	@Test
	public void test() {
		// test 1
		Result<Optional<Token>> result = this.expr.parse(this.context);
		assertEquals("mismatched result", false, result.get().isPresent());

		// test 2
		this.initContext("hello");
		Token expected = this.input.createToken(0, 5);
		result = this.expr.parse(this.context);
		assertEquals("mismatched result", expected, result.get().get());
	}

}
