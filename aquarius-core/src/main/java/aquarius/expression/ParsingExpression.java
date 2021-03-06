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
import aquarius.action.ParsingAction.Consumer;
import aquarius.action.ParsingAction.Mapper;

public interface ParsingExpression<R> {
    <T> T accept(ExpressionVisitor<T> visitor);

    /**
     * @param context
     * @return return true if parsing success.
     */
    boolean parse(ParserContext context);

    /**
     * @return return true if this operator construct some non null value.
     */
    boolean isReturnable();

    default <E> Action<E, R> map(Mapper<E, R> action) {
        return new Action<>(this, action);
    }

    default Action<Void, R> consume(Consumer<R> action) {
        return new Action<>(this, action);
    }

    /**
     * helper method for the construction of ZeroMore
     *
     * @return
     */
    default ZeroMore<R> zeroMore() {
        return new ZeroMore<>(this);
    }

    /**
     * helper method for the construction of OneMore
     *
     * @return
     */
    default OneMore<R> oneMore() {
        return new OneMore<>(this);
    }

    /**
     * helper method for the construction of Optional
     *
     * @return
     */
    default Optional<R> opt() {
        return new Optional<>(this);
    }

    /**
     * helper method for the construction of Choice
     *
     * @param expr
     * @return
     */
    @SuppressWarnings("unchecked")
    default Choice<R> or(ParsingExpression<? extends R> expr) {
        if(this instanceof Choice) {
            ParsingExpression<?>[] alters = ((Choice<?>) this).getExprs();
            int size = alters.length;
            ParsingExpression<?>[] exprs = new ParsingExpression<?>[size + 1];
            System.arraycopy(alters, 0, exprs, 0, size);
            exprs[size] = expr;
            return new Choice<>((ParsingExpression<R>[]) exprs);
        }
        return new Choice<>(this, (ParsingExpression<R>) expr);
    }
}
