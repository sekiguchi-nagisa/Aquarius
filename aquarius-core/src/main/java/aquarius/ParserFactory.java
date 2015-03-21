/*
 * Copyright (C) 2014-2015 Nagisa Sekiguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*
 * Copyright (C) 2015 Nagisa Sekiguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package aquarius;

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

import aquarius.Parser.PatternWrapper;
import aquarius.annotation.Grammar;
import aquarius.annotation.RuleDefinition;

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
					&& matchMethod(method, Rule.class, null)) {
				try {
					method.invoke(instance);
				} catch(IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * match instance method name, return class, parameter
	 * @param targetMethod
	 * @param returnClass
	 * @param methodName
	 * if null, accept any method name
	 * @param paramClasses
	 * @return
	 */
	private static boolean matchMethod(Method targetMethod, Class<?> returnClass, String methodName, Class<?>... paramClasses) {
		// compare method name
		if(methodName != null && !methodName.equals(targetMethod.getName())) {
			return false;
		}

		// compare parameter classes
		Class<?>[] targetParamClasses = targetMethod.getParameterTypes();
		int size = paramClasses.length;
		if(targetParamClasses.length != size) {
			return false;
		}

		for(int i = 0; i < size; i++) {
			if(!paramClasses[i].equals(targetParamClasses[i])) {
				return false;
			}
		}

		// compare return class
		return returnClass.equals(targetMethod.getReturnType());
	}

	private static class ParserProxy implements InvocationHandler {
		private boolean invokeRule = true;
		private int ruleIndexCount = 0;
		private final Map<String, Rule<?>> ruleMap;

		public ParserProxy() {
			this.ruleMap = new HashMap<>();
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Annotation[] annos = method.getAnnotations();
			String methodName = method.getName();

			// call user defined Rule method
			if(matchAnnotation(annos, RuleDefinition.class) && matchMethod(method, Rule.class, null)) {
				return this.invokeRule ? 
						this.createRule(proxy, method, args) : this.getCachedRule(methodName);
			}

			// invoke Parser#rule(Pattern pattern)
			if(this.invokeRule && method.isDefault()) {
				if(matchMethod(method, Rule.class, "rule", PatternWrapper.class) 
						|| matchMethod(method, Rule.class, "ruleVoid", PatternWrapper.class)) {
					PatternWrapper<?> pattern = (PatternWrapper<?>)args[0];
					boolean returnable = methodName.equals("rule");
					Rule<?> expr = new Rule<>(this.ruleIndexCount++, pattern, returnable);
					return expr;
				}
			}

			// hashCode, equals, toString
			if(matchMethod(method, int.class, "hashCode")) {
				return this.hashCode();
			} else if(matchMethod(method, boolean.class, "equals", Object.class)) {
				return proxy == args[0];
			} else if(matchMethod(method, String.class, "toString")) {
				return this.toString();
			}

			// call default method
			if(method.isDefault() && !matchMethod(method, Rule.class, "rule", PatternWrapper.class) 
					&& !matchMethod(method, Rule.class, "ruleVoid", PatternWrapper.class)) {
				return this.invokeDefaultMethod(proxy, method, args);
			}

			// otherwise
			throw new IllegalArgumentException("unimplemented method: " + method);
		}

		@SuppressWarnings("unchecked")
		private final <T> T invokeDefaultMethod(Object proxy, Method method, 
				Object[] args, Class<T> returnClass) throws Throwable {
			return (T) this.invokeDefaultMethod(proxy, method, args);
		}

		private final Object invokeDefaultMethod(Object proxy, Method method, 
				Object[] args) throws Throwable {
			if(!method.isDefault()) {
				throw new IllegalArgumentException("must be default method: " + method.getName());
			}
			Class<?> declaringClass = method.getDeclaringClass();
			final Constructor<MethodHandles.Lookup> constructor = 
					MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
			if(!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}

			// invoke
			return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
					.unreflectSpecial(method, declaringClass)
					.bindTo(proxy).invokeWithArguments(args);
		}

		private final Rule<?> createRule(Object proxy, Method method, Object[] args) throws Throwable {
			Rule<?> value = this.invokeDefaultMethod(proxy, method, args, Rule.class);
			this.ruleMap.put(method.getName(), value);
			return value;
		}

		private final Rule<?> getCachedRule(String ruleName) {
			Rule<?> rule = this.ruleMap.get(ruleName);
			if(rule == null) {
				throw new RuntimeException("not found rule: " + ruleName);
			}
			return rule;
		}

		public void initializeRules() {
			this.invokeRule = false;
			for(Entry<String, Rule<?>> entry : this.ruleMap.entrySet()) {
				entry.getValue().init(this.ruleIndexCount);
			}
		}
	}
}
