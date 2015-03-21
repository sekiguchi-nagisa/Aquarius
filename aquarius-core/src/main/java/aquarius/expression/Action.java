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

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.action.FailedActionException;
import aquarius.action.ParsingAction;
import aquarius.action.ParsingAction.*;
import aquarius.misc.Utils;

/**
* try to match the expression, if success execute action. preceding expression result
* is treated as the argument of action.
* -> expr { action }
* @author skgchxngsxyz-opensuse
 * @param <A>
 * @param <A>
*
*/
public class Action<R, A> implements ParsingExpression<R> {	// extended expression type
	private final ParsingExpression<A> expr;
	private final ParsingAction<R, A> action;
	private final boolean returnable;

	public Action(ParsingExpression<A> expr, ParsingAction<R, A> action) {
		this.expr = expr;
		this.action = action;
		this.returnable = action instanceof Mapper;
	}

	public ParsingExpression<A> getExpr() {
		return this.expr;
	}

	public ParsingAction<R, A> getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "{ action }";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean parseImpl(ParserContext context) {
		// parser preceding expression
		if(!this.expr.parseImpl(context)) {
			return false;
		}

		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// invoke action
		try {
			if(this.action instanceof Mapper) {
				context.pushValue(
					((Mapper<R, A>) this.action).invoke(context, (A) context.popValue()));
			} else {
				((Consumer<A>) this.action).invoke(context, (A) context.popValue());
			}
			return true;
		} catch(FailedActionException e) {
			context.pushFailure(pos, e);
			return false;
		} catch(Exception e) {
			return Utils.propagate(e);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAction(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}