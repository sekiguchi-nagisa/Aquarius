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

package aquarius;

import aquarius.CacheFactory.CacheKind;
import aquarius.expression.ParsingExpression;

@FunctionalInterface
public interface Rule<R> extends ParsingExpression<R> {
    @Override
    default <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitRule(this);
    }

    @Override
    default boolean parseImpl(ParserContext context) {
        return false;
    }

    @Override
    default boolean isReturnable() {
        return false;
    }

    default int getRuleIndex() {
        return 0;
    }

    default ParsingExpression<R> getPattern() {
        return null;
    }

    default void init(int ruleSize) {
    }

    default ParsedResult<R> parse(AquariusInputStream input) {
        return this.parse(input, new CacheFactory(CacheKind.Limit));
    }

    default ParsedResult<R> parse(AquariusInputStream input, CacheFactory factory) {
        return null;
    }

    ParsingExpression<R> invoke();
}

class RuleImpl<R> implements Rule<R> {
    /**
     * for memoization
     */
    private final int ruleIndex;
    private final boolean returnable;
    /**
     * for initialization of memo
     */
    private int ruleSize;
    /**
     * will be null after call initExpr()
     */
    private Rule<R> wrapper;

    private ParsingExpression<R> pattern;

    public RuleImpl(int ruleIndex, Rule<R> wrapper, boolean returnable) {
        this.ruleIndex = ruleIndex;
        this.wrapper = wrapper;
        this.returnable = returnable;
    }

    @Override
    public boolean parseImpl(ParserContext context) {
        AquariusInputStream input = context.getInputStream();
        ResultCache cache = context.getCache();

        final int ruleIndex = this.ruleIndex;
        final int srcPos = input.getPosition();

        CacheEntry entry = cache.get(ruleIndex, srcPos);
        if(entry != null) {
            if(!entry.getStatus()) {
                context.pushValue(null);
                return false;
            }
            input.setPosition(entry.getCurrentPos());
            context.pushValue(entry.getValue());
            return true;
        }
        // if not found previous parsed result, invoke rule
        boolean status = this.pattern.parseImpl(context);
        if(status) {
            cache.set(ruleIndex, srcPos, context.getValue(), input.getPosition());
        } else {
            cache.setFailure(ruleIndex, srcPos);
        }
        return status;
    }

    @Override
    public boolean isReturnable() {
        return this.returnable;
    }

    @Override
    public void init(int ruleSize) {
        if(this.pattern == null) {
            this.ruleSize = ruleSize;
            this.pattern = this.wrapper.invoke();
            this.wrapper = null;
        }
    }

    @Override
    public int getRuleIndex() {
        return this.ruleIndex;
    }

    @Override
    public ParsingExpression<R> getPattern() {
        return this.pattern;
    }

    @Override
    public ParsedResult<R> parse(AquariusInputStream input, CacheFactory factory) {
        ParserContext context = new ParserContext(input, factory.newCache(this.ruleSize));

        // start parsing
        boolean status = this.pattern.parse(context);

        // create result
        return new ParsedResult<>(status ? context.popValue() : context.getFailure());
    }

    @Override
    public ParsingExpression<R> invoke() {
        return null;    // do nothing
    }
}
