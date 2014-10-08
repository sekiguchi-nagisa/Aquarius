package aquarius.combinator;

import aquarius.runtime.ParsedResult;

@FunctionalInterface
public interface ParsingAction {
	public ParsedResult invoke(ParsedResult... args);
}
