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

import java.util.LinkedList;
import java.util.List;

/**
 * match zero or more repetition of the expression. return matched results as array
 * -> expr *
 *
 * @param <R>
 * @author skgchxngsxyz-opensuse
 */
public class ZeroMore<R> implements ParsingExpression<List<R>> {
    private final ParsingExpression<R> expr;
    private final boolean returnable;

    public ZeroMore(ParsingExpression<R> expr) {
        this.expr = expr;
        this.returnable = expr.isReturnable();
    }

    public ParsingExpression<R> getExpr() {
        return this.expr;
    }

    @Override
    public String toString() {
        return this.expr.toString() + "*";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean parse(ParserContext context) {
        List<R> result = this.returnable ? new LinkedList<>() : null;

        while(this.expr.parse(context)) {
            if(this.returnable) {
                result.add((R) context.popValue());
            }
        }
        context.pushValue(result);
        return true;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitZeroMore(this);
    }

    @Override
    public boolean isReturnable() {
        return this.returnable;
    }
}