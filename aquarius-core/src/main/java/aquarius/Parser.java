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

import aquarius.expression.ParsingExpression;

/**
 * if define new parse class, must be extends this interface.
 * @author skgchxngsxyz-opensuse
 *
 */
public interface Parser {
	/**
	 * create and set new parsing rule which constructing non null value. must not be override.
	 * @param expr
	 * right hand side expression of new parsing rule.
	 * @return
	 */
	public default <R> Rule<R> rule(PatternWrapper<R> expr) {
		throw new RuntimeException("forbidden invocation: rule");
	}

	/**
	 * create and set new parsing rule, must not be override.
	 * @param expr
	 * right hand side expression of new parsing rule.
	 * @return
	 */
	public default Rule<Void> ruleVoid(PatternWrapper<Void> expr) {
		throw new RuntimeException("forbidden invocation: ruleVoid");
	}

	@FunctionalInterface
	public static interface PatternWrapper<R> {
		public ParsingExpression<R> invoke();
	}
}
