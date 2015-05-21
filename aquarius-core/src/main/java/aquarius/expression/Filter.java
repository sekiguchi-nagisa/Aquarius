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
import aquarius.misc.Tuple3;
import aquarius.misc.Tuple4;
import aquarius.misc.Tuple5;

public class Filter<R> implements ParsingExpression<R> {
    private final int targetKind;
    private final ParsingExpression<?> expr;

    public Filter(int targetKind, ParsingExpression<?> expr) {
        this.targetKind = targetKind;
        this.expr = expr;
    }

    public static Filter<?> filter0(ParsingExpression<?> expr) {
        return new Filter<>(0, expr);
    }

    public static Filter<?> filter1(ParsingExpression<?> expr) {
        return new Filter<>(1, expr);
    }

    public static Filter<?> filter2(ParsingExpression<?> expr) {
        return new Filter<>(2, expr);
    }

    public static Filter<?> filter3(ParsingExpression<?> expr) {
        return new Filter<>(3, expr);
    }

    public static Filter<?> filter4(ParsingExpression<?> expr) {
        return new Filter<>(4, expr);
    }

    public int getTargetKind() {
        return this.targetKind;
    }

    public ParsingExpression<?> getExpr() {
        return this.expr;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitFilter(this);
    }

    @Override
    public boolean parseImpl(ParserContext context) {
        if(!this.expr.parseImpl(context)) {
            return false;
        }
        Object value = context.popValue();
        switch(this.targetKind) {
        case 0:
            context.pushValue(((Tuple2<?, ?>) value).get0());
            break;
        case 1:
            context.pushValue(((Tuple2<?, ?>) value).get1());
            break;
        case 2:
            context.pushValue(((Tuple3<?, ?, ?>) value).get2());
            break;
        case 3:
            context.pushValue(((Tuple4<?, ?, ?, ?>) value).get3());
            break;
        case 4:
            context.pushValue(((Tuple5<?, ?, ?, ?, ?>) value).get4());
            break;
        default:
            throw new AssertionError("unsupported target kind: " + this.targetKind);
        }
        return true;
    }

    @Override
    public boolean isReturnable() {
        return true;
    }

}
