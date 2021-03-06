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

import aquarius.action.PredictiveAction;
import aquarius.expression.*;
import aquarius.misc.Tuple2;
import aquarius.misc.Tuple3;
import aquarius.misc.Tuple4;
import aquarius.misc.Tuple5;

/**
 * helper methods for parsing expression construction
 *
 * @author skgchxngsxyz-opensuse
 */
public final class Expressions {
    public final static Any ANY = new Any();
    public final static NotPredict EOF = not(ANY);
    public final static ZeroMore<Void> WS = ch(' ', '\t', '\r', '\n').zeroMore();
    public final static ZeroMore<Void> __ = WS;
    private Expressions() {
    }

    public static Literal str(String target) {
        return Literal.newLiteral(target);
    }

    public static CharSet ch(char... chars) {
        return CharSet.newCharSet(chars);
    }

    public static CharSet ch(int... chars) {
        return CharSet.newCharSet(chars);
    }

    public static CharSet r(char start, char stop) {
        return CharSet.newCharSet(new int[]{}).r(start, stop);
    }

    public static CharSet r(int start, int stop) {
        return CharSet.newCharSet(new int[]{}).r(start, stop);
    }

    // ZeroMore
    public static <A, B> ZeroMore<Tuple2<A, B>> zeroMore(ParsingExpression<A> a, ParsingExpression<B> b) {
        return new ZeroMore<>(new Sequence2<>(a, b));
    }

    public static <A, B, C> ZeroMore<Tuple3<A, B, C>> zeroMore(ParsingExpression<A> a,
                                                                     ParsingExpression<B> b, ParsingExpression<C> c) {
        return new ZeroMore<>(new Sequence3<>(a, b, c));
    }

    public static <A, B, C, D> ZeroMore<Tuple4<A, B, C, D>> zeroMore(ParsingExpression<A> a,
                                                                           ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d) {
        return new ZeroMore<>(new Sequence4<>(a, b, c, d));
    }

    public static <A, B, C, D, E> ZeroMore<Tuple5<A, B, C, D, E>> zeroMore(ParsingExpression<A> a,
                                                                                 ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
        return new ZeroMore<>(new Sequence5<>(a, b, c, d, e));
    }

    // OneMore
    public static <A, B> OneMore<Tuple2<A, B>> oneMore(ParsingExpression<A> a, ParsingExpression<B> b) {
        return new OneMore<>(new Sequence2<>(a, b));
    }

    public static <A, B, C> OneMore<Tuple3<A, B, C>> oneMore(ParsingExpression<A> a,
                                                                   ParsingExpression<B> b, ParsingExpression<C> c) {
        return new OneMore<>(new Sequence3<>(a, b, c));
    }

    public static <A, B, C, D> OneMore<Tuple4<A, B, C, D>> oneMore(ParsingExpression<A> a,
                                                                         ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d) {
        return new OneMore<>(new Sequence4<>(a, b, c, d));
    }

    public static <A, B, C, D, E> OneMore<Tuple5<A, B, C, D, E>> oneMore(ParsingExpression<A> a,
                                                                               ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
        return new OneMore<>(new Sequence5<>(a, b, c, d, e));
    }

    // Optional
    public static <A, B> Optional<Tuple2<A, B>> opt(ParsingExpression<A> a, ParsingExpression<B> b) {
        return new Optional<>(new Sequence2<>(a, b));
    }

    public static <A, B, C> Optional<Tuple3<A, B, C>> opt(ParsingExpression<A> a,
                                                                ParsingExpression<B> b, ParsingExpression<C> c) {
        return new Optional<>(new Sequence3<>(a, b, c));
    }

    public static <A, B, C, D> Optional<Tuple4<A, B, C, D>> opt(ParsingExpression<A> a,
                                                                      ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d) {
        return new Optional<>(new Sequence4<>(a, b, c, d));
    }

    public static <A, B, C, D, E> Optional<Tuple5<A, B, C, D, E>> opt(ParsingExpression<A> a,
                                                                            ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
        return new Optional<>(new Sequence5<>(a, b, c, d, e));
    }

    // And Predict
    public static AndPredict and(ParsingExpression<?> expr) {
        return new AndPredict(expr);
    }

    public static AndPredict and(ParsingExpression<?>... exprs) {
        return new AndPredict(new Sequence(exprs));
    }

    // Not Predict
    public static NotPredict not(ParsingExpression<?> expr) {
        return new NotPredict(expr);
    }

    public static NotPredict not(ParsingExpression<?>... exprs) {
        return new NotPredict(new Sequence(exprs));
    }

    @SafeVarargs
    public static Sequence seqN(ParsingExpression<?>... exprs) {
        return new Sequence(exprs);
    }

    public static <A, B> Sequence2<A, B> seq(ParsingExpression<A> a, ParsingExpression<B> b) {
        return new Sequence2<>(a, b);
    }

    public static <A, B, C> Sequence3<A, B, C> seq(ParsingExpression<A> a, ParsingExpression<B> b,
                                                         ParsingExpression<C> c) {
        return new Sequence3<>(a, b, c);
    }

    public static <A, B, C, D> Sequence4<A, B, C, D> seq(ParsingExpression<A> a, ParsingExpression<B> b,
                                                               ParsingExpression<C> c, ParsingExpression<D> d) {
        return new Sequence4<>(a, b, c, d);
    }

    public static <A, B, C, D, E> Sequence5<A, B, C, D, E> seq(ParsingExpression<A> a, ParsingExpression<B> b,
                                                                     ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
        return new Sequence5<>(a, b, c, d, e);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <R> Choice<R> or(ParsingExpression<? extends R>... exprs) {
        return new Choice<>((ParsingExpression<R>[]) exprs);
    }

    public static PredictAction predict(PredictiveAction action) {
        return new PredictAction(action);
    }

    @SafeVarargs
    public static Capture $(ParsingExpression<?>... exprs) {
        return new Capture(exprs);
    }

    public static Capture $(String target) {
        return new Capture(Literal.newLiteral(target));
    }

    public static Capture $(char... chars) {
        return new Capture(CharSet.newCharSet(chars));
    }

    public static Capture $(int... chars) {
        return new Capture(CharSet.newCharSet(chars));
    }
}
