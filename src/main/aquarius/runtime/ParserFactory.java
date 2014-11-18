package aquarius.runtime;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import aquarius.matcher.NoneTerminal;
import aquarius.matcher.Parser;
import aquarius.matcher.Parser.Pattern;
import aquarius.runtime.annotation.Grammar;
import aquarius.runtime.annotation.RuleDefinition;

public class ParserFactory {
	@SuppressWarnings("unchecked")
	public final static <R extends Parser> R createParser(Class<R> baseParserClass) {
		verifyParserClass(baseParserClass);

		ParserProxy proxy = new ParserProxy();
		R parserInstance = (R) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), 
				new Class[]{baseParserClass}, proxy);

		invokeRuleMethods(parserInstance, baseParserClass.getDeclaredMethods());

		proxy.initializeRules();
		return parserInstance;
	}

	private static void verifyParserClass(Class<? extends Parser> parserClass) {
		if(!parserClass.isInterface() 
				|| !Modifier.isPublic(parserClass.getModifiers())
				|| parserClass.getInterfaces().length != 1
				|| !parserClass.getInterfaces()[0].equals(Parser.class)
				|| !matchAnnotation(parserClass.getAnnotations(), Grammar.class)) {
			throw new IllegalArgumentException("illegal parser class: " + parserClass.getName());
		}
	}

	private static boolean matchAnnotation(Annotation[] annos, Class<? extends Annotation> clazz) {
		if(annos != null) {
			for(Annotation anno : annos) {
				if(anno.annotationType().equals(clazz)) {
					return true;
				}
			}
		}
		return false;
	}

	private static void invokeRuleMethods(Object instance, Method[] methods) {
		for(Method method : methods) {
			if(matchAnnotation(method.getAnnotations(), RuleDefinition.class) 
					&& method.getReturnType().equals(NoneTerminal.class) 
					&& matchParameter(new Class[]{}, method.getParameterTypes())) {
				try {
					method.invoke(instance);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private static boolean matchParameter(Class<?>[] paramClasses, Class<?>[] targetParamClasses) {
		if(paramClasses == null || targetParamClasses == null) {
			return false;
		}
		final int size = paramClasses.length;
		if(targetParamClasses.length != size) {
			return false;
		}
		for(int i = 0; i < size; i++) {
			if(!paramClasses[i].equals(targetParamClasses[i])) {
				return false;
			}
		}
		return true;
	}

	private static class ParserProxy implements InvocationHandler {
		private boolean allowDefaultMethodInvocation = true;
		private int ruleIndex = 0;
		private final Map<String, NoneTerminal<?>> ruleMap;

		public ParserProxy() {
			this.ruleMap = new HashMap<>();
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Annotation[] annos = method.getAnnotations();
			String methodName = method.getName();
			if(matchAnnotation(annos, RuleDefinition.class)) {
				return this.allowDefaultMethodInvocation ? 
						this.createRule(proxy, method, args) : this.getCachedRule(methodName);
			}

			// invoke Parser#rule(Pattern pattern)
			if(this.allowDefaultMethodInvocation && methodName.equals("rule")) {
				Pattern<?> pattern = (Pattern<?>)args[0];
				NoneTerminal<?> expr = new NoneTerminal<>(this.ruleIndex++, pattern);
				return expr;
			}
			return null;
		}

		private final NoneTerminal<?> createRule(Object proxy, Method method, Object[] args) throws Throwable {
			if(!method.isDefault()) {
				throw new RuntimeException("must be default method: " + method.getName());
			}
			Class<?> declaringClass = method.getDeclaringClass();
			final Constructor<MethodHandles.Lookup> constructor = 
					MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
			if(!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}
			NoneTerminal<?> value = (NoneTerminal<?>) constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
					.unreflectSpecial(method, declaringClass)
					.bindTo(proxy).invokeWithArguments(args);
			this.ruleMap.put(method.getName(), value);
			return value;
		}

		private final NoneTerminal<?> getCachedRule(String ruleName) {
			NoneTerminal<?> rule = this.ruleMap.get(ruleName);
			if(rule == null) {
				throw new RuntimeException("not found rule: " + ruleName);
			}
			return rule;
		}

		public void initializeRules() {
			this.allowDefaultMethodInvocation = false;
			for(Entry<String, NoneTerminal<?>> entry : this.ruleMap.entrySet()) {
				entry.getValue().initExpr();
			}
		}
	}
}
