package aquarius.runtime;

@FunctionalInterface
public interface ParsingAction {
	public ParsedResult invoke(ParsedResult... args);
}
