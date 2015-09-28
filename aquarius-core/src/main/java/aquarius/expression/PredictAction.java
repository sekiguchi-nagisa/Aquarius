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
import aquarius.action.PredictiveAction;
import aquarius.misc.Utils;

/**
 * execute semantic action. if return value is not failed, not advance parsing position.
 * otherwise, match failed.
 * -> & { action }
 *
 * @author skgchxngsxyz-opensuse
 */
public class PredictAction implements ParsingExpression<Void> {    // extended expression type
    private final PredictiveAction action;

    public PredictAction(PredictiveAction action) {
        this.action = action;
    }

    public PredictiveAction getAction() {
        return this.action;
    }

    @Override
    public String toString() {
        return "&{ action }";
    }

    @Override
    public boolean parseImpl(ParserContext context) {
        AquariusInputStream input = context.getInputStream();
        int pos = input.getPosition();

        try {
            boolean status = this.action.invoke(context);

            if(!status) {
                context.pushFailure(pos, this);
            }
            input.setPosition(pos);
            return status;    //if prediction is success, return true
        } catch(Exception e) {
            return Utils.propagate(e);
        }
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitPredictAction(this);
    }

    @Override
    public boolean isReturnable() {
        return false;
    }
}