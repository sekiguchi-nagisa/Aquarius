package aquarius.runtime;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import aquarius.matcher.NoneTerminal;
import aquarius.matcher.Parser;

public class ParserFactory {
	@SuppressWarnings("unchecked")
	public final static <R extends Parser> R createParser(Class<R> baseParserClass) {
		final Map<String, NoneTerminal<?>> ruleMap = new HashMap<>();
		return (R) Proxy.newProxyInstance(baseParserClass.getClassLoader(), 
				new Class[]{baseParserClass}, (proxy, method, args) -> {
			ruleMap.clear();
			return null;
		});
	}
}
