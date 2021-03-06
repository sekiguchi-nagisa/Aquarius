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
import aquarius.action.FailedActionException;
import aquarius.misc.Utils;

/**
 * for user defined operator. must return non null value.
 *
 * @param <R>
 * @author skgchxngsxyz-opensuse
 */
@FunctionalInterface
public interface CustomExpr<R> extends ParsingExpression<R> {
    default <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitCustomExpr(this);
    }

    default boolean parse(ParserContext context) {
        AquariusInputStream input = context.getInputStream();
        int pos = input.getPosition();

        try {
            context.pushValue(this.apply(context));
            return true;
        } catch(FailedActionException e) {
            context.pushFailure(pos, e);
            return false;
        } catch(Exception e) {
            return Utils.propagate(e);
        }
    }

    default boolean isReturnable() {
        return true;
    }

    /**
     * user defined operator implementation.
     *
     * @param context
     * @return constructed value of this operator. must not be null.
     * @throws FailedActionException
     * @throws Exception
     */
    R apply(ParserContext context) throws FailedActionException, Exception;
}
