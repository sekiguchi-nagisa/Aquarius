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

import aquarius.expression.*;

public interface ExpressionVisitor<T> {
    <R, A> T visitAction(Action<R, A> expr);

    T visitAndPredict(AndPredict expr);

    T visitAny(Any expr);

    T visitCapture(Capture expr);

    T visitCharSet(CharSet expr);

    <R> T visitChoice(Choice<R> expr);

    <R> T visitCustomExpr(CustomExpr<R> expr);

    <R> T visitFilter(Filter<R> expr);

    T visitLiteral(Literal expr);

    T visitNotPredict(NotPredict expr);

    <R> T visitOneMore(OneMore<R> expr);

    <R> T visitOptional(Optional<R> expr);

    T visitPredictAction(PredictAction expr);

    <R> T visitRule(Rule<R> expr);

    T visitSequence(Sequence expr);

    <A, B> T visitSequence2(Sequence2<A, B> expr);

    <A, B, C> T visitSequence3(Sequence3<A, B, C> expr);

    <A, B, C, D> T visitSequence4(Sequence4<A, B, C, D> expr);

    <A, B, C, D, E> T visitSequence5(Sequence5<A, B, C, D, E> expr);

    T visitVoidCustomExpr(VoidCustomExpr expr);

    <R> T visitZeroMore(ZeroMore<R> expr);
}
