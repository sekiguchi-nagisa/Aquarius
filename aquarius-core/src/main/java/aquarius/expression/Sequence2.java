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
import aquarius.misc.Tuple2;

import static aquarius.misc.Tuples.of;

/**
 * try to match the sequence of expressions and return matched results as tuple2.
 *
 * @param <A>
 * @param <B>
 * @author skgchxngsxyz-opensuse
 */
public class Sequence2<A, B> implements ParsingExpression<Tuple2<A, B>> {
    private final Tuple2<ParsingExpression<A>, ParsingExpression<B>> exprs;
    private final boolean returnable;

    public Sequence2(ParsingExpression<A> a, ParsingExpression<B> b) {
        this.exprs = of(a, b);
        this.returnable = a.isReturnable() || b.isReturnable();
    }

    public Tuple2<ParsingExpression<A>, ParsingExpression<B>> getExprs() {
        return this.exprs;
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(this.exprs.get0());
        sBuilder.append(' ');
        sBuilder.append(this.exprs.get1());
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

                if(this.returnable) {
                    context.pushValue(of(a, b));
                }
                return true;
            }
        }
        context.getInputStream().setPosition(pos);
        return false;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitSequence2(this);
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
}