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
import aquarius.misc.Tuple3;

import static aquarius.misc.Tuples.of;

/**
 * try to match the sequence of expressions and return matched results as tuple3.
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @author skgchxngsxyz-opensuse
 */
public class Sequence3<A, B, C> implements ParsingExpression<Tuple3<A, B, C>> {
    private final Tuple3<ParsingExpression<A>, ParsingExpression<B>, ParsingExpression<C>> exprs;
    private final boolean returnable;

    public Sequence3(ParsingExpression<A> a, ParsingExpression<B> b, ParsingExpression<C> c) {
        this.exprs = of(a, b, c);
        this.returnable = a.isReturnable() || b.isReturnable() || c.isReturnable();
    }

    public Tuple3<ParsingExpression<A>, ParsingExpression<B>, ParsingExpression<C>> getExprs() {
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
        return sBuilder.toString();
    }

    @Override
    public boolean parse(ParserContext context) {
        int pos = context.getInputStream().getPosition();
        if(this.exprs.get0().parse(context)) {
            @SuppressWarnings("unchecked")
            A a = (A) context.popValue();

            if(this.exprs.get1().parse(context)) {
                @SuppressWarnings("unchecked")
                B b = (B) context.popValue();

                if(this.exprs.get2().parse(context)) {
                    @SuppressWarnings("unchecked")
                    C c = (C) context.popValue();

                    if(this.returnable) {
                        context.pushValue(of(a, b, c));
                    }
                    return true;
                }
            }
        }
        context.getInputStream().setPosition(pos);
        return false;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitSequence3(this);
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
}