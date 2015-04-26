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

import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.misc.Tuple4;
import static aquarius.misc.Tuples.*;

/**
 * try to match the sequence of expressions and return matched results as tuple4.
 * @author skgchxngsxyz-opensuse
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 */
public class Sequence4<A, B, C, D> implements ParsingExpression<Tuple4<A, B, C, D>> {
	private final Tuple4<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>> exprs;
	private final boolean returnable;

	public Sequence4(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c, ParsingExpression<D> d) {
		this.exprs = of(a, b, c, d);
		this.returnable = a.isReturnable() || b.isReturnable() 
				|| c.isReturnable() || d.isReturnable();
	}

	public Tuple4<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>> getExprs() {
		return this.exprs;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.exprs.get0());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get1());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get2());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get3());
		return sBuilder.toString();
	}

	@Override
	public boolean parseImpl(ParserContext context) {
		if(this.exprs.get0().parseImpl(context)) {
			@SuppressWarnings("unchecked")
			A a = (A) context.popValue();

			if(this.exprs.get1().parseImpl(context)) {
				@SuppressWarnings("unchecked")
				B b = (B) context.popValue();

				if(this.exprs.get2().parseImpl(context)) {
					@SuppressWarnings("unchecked")
					C c = (C) context.popValue();

					if(this.exprs.get3().parseImpl(context)) {
						@SuppressWarnings("unchecked")
						D d = (D) context.popValue();

						if(this.returnable) {
							context.pushValue(of(a, b, c, d));
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence4(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}

	// for filter
	@SuppressWarnings("unchecked")
	public Filter<A> filter0() {
		return (Filter<A>) Filter.filter0(this);
	}

	@SuppressWarnings("unchecked")
	public Filter<B> filter1() {
		return (Filter<B>) Filter.filter1(this);
	}

	@SuppressWarnings("unchecked")
	public Filter<C> filter2() {
		return (Filter<C>) Filter.filter2(this);
	}

	@SuppressWarnings("unchecked")
	public Filter<D> filter3() {
		return (Filter<D>) Filter.filter3(this);
	}
}