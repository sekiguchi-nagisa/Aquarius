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
import aquarius.Token;

/**
 * try to match sub expression sequence and return matched result as one token.
 * -> < expr1 expr2 ... exprN >
 *
 * @author skgchxngsxyz-opensuse
 */
public class Capture implements ParsingExpression<Token> {    // extended expression type
    private final ParsingExpression<?>[] exprs;

    @SafeVarargs
    public Capture(ParsingExpression<?>... exprs) {
        this.exprs = exprs;
    }

    public ParsingExpression<?>[] getExprs() {
        return this.exprs;
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append('<');
        final int size = this.exprs.length;
        for(int i = 0; i < size; i++) {
            if(i > 0) {
                sBuilder.append(' ');
            }
            sBuilder.append(this.exprs[i]);
        }
        sBuilder.append('>');
        return sBuilder.toString();
    }

    @Override
    public boolean parse(ParserContext context) {
        AquariusInputStream input = context.getInputStream();
        int pos = input.getPosition();
        for(ParsingExpression<?> e : this.exprs) {
            if(!e.parse(context)) {
                input.setPosition(pos);
                return false;
            }
        }
        context.pushValue(input.createToken(pos));
        return true;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitCapture(this);
    }

    @Override
    public boolean isReturnable() {
        return true;
    }
}