package aquarius.example;

import static org.junit.Assert.*;

import org.junit.Test;

import aquarius.AquariusInputStream;
import aquarius.CommonStream;
import aquarius.ParsedResult;
import aquarius.ParserFactory;
import aquarius.Token;

public class SampleParserTest {
	@Test
	public void test() {
		SampleParser parser = ParserFactory.createParser(SampleParser.class);

		String source = "      (1+ 3) / 2      ";
		AquariusInputStream input = new CommonStream("ex", source);

		ParsedResult<Token> result = parser.Expr().parse(input);
		assertTrue(result.isSucess());
		assertEquals(input.getPosition(), input.getInputSize());
		assertEquals(result.getValue().getText(input), source.trim());

		// parser instance test
		assertNotNull(parser.toString());
		assertTrue(parser.equals(parser));
		assertEquals(parser.hashCode(), parser.hashCode());

		try {
			assertNull(parser.rule(null));
			fail();
		} catch(IllegalArgumentException e) {
		}

		try {
			assertNull(parser.ruleVoid(null));
			fail();
		} catch(IllegalArgumentException e) {
		}
	}
}
