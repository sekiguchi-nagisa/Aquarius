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
 */

package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* try to match the expression. if not success, not advance parsing position.
* otherwise, match failed.
* -> ! expr
* @author skgchxngsxyz-opensuse
*
*/
public class NotPredict implements ParsingExpression<Void> {
	private final ParsingExpression<?> expr;

	public NotPredict(ParsingExpression<?> expr) {
		this.expr = expr;
	}

	public ParsingExpression<?> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return "!" + this.expr.toString();
	}

	@Override
	public boolean parseImpl(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		context.setFailureCreation(false);
		boolean status = !this.expr.parseImpl(context);
		context.setFailureCreation(true);

		if(!status) {
			context.pushFailure(pos, this);
		}
		input.setPosition(pos);
		return status;	//if prediction is failed, return true
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredict(this);
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}