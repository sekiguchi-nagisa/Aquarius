/*
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

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* try to match first expression and if failed try the second one ...
* -> expr1 / expr2 / ... / exprN
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class Choice<R> implements ParsingExpression<R> {
	private final ParsingExpression<R>[] exprs;
	private final boolean returnable;

	@SafeVarargs
	public Choice(ParsingExpression<R> ...exprs) {
		this.exprs = exprs;
		this.returnable = exprs[0].isReturnable();
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		final int size = this.exprs.length;
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(" / ");
			}
			sBuilder.append(this.exprs[i]);
		}
		return sBuilder.toString();
	}

	@Override
	public boolean parseImpl(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();
		for(ParsingExpression<R> e : this.exprs) {
			input.setPosition(pos);
			if(e.parseImpl(context)) {
				return true;
			}
		}
		return false;
	}

	public ParsingExpression<R>[] getExprs() {
		return this.exprs;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitChoice(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}