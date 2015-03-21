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


package aquarius.expression;

import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* try to match the expression. return matched result as java.util.Optional
* -> expr ?
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class Optional<R> implements ParsingExpression<java.util.Optional<R>> {
	private final ParsingExpression<R> expr;
	private final boolean returnable;

	public Optional(ParsingExpression<R> expr) {
		this.expr = expr;
		this.returnable = expr.isReturnable();
	}

	public ParsingExpression<R> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return this.expr.toString() + "?";
	}

	@Override
	public boolean parseImpl(ParserContext context) {
		context.setFailureCreation(false);
		this.expr.parse(context);
		if(this.returnable) {
			context.pushValue(java.util.Optional.ofNullable(context.popValue()));
		}
		context.setFailureCreation(true);
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOptional(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}