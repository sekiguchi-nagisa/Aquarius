package aquarius.matcher.expression;

import static aquarius.matcher.expression.ParsingExpression.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;

public class SequenceTest extends TestBase<List<Token>> {
	@Before
	public void prepare() {
		this.expr = seq(ch('\t', ' '), str("hello"), any(), str("world"));
		this.initContext("\thello world");
	}

	@Test
	public void test() {
		Result<List<Token>> result = this.expr.parse(this.context);
		assertEquals("mismatched result", 4,       result.get().size());
		assertEquals("mismatched result", "\t",    result.get().get(0).getText(this.input));
		assertEquals("mismatched result", "hello", result.get().get(1).getText(this.input));
		assertEquals("mismatched result", " ",     result.get().get(2).getText(this.input));
		assertEquals("mismatched result", "world", result.get().get(3).getText(this.input));
	}

}
