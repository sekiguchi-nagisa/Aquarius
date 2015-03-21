/*
 * Copyright (C) 2015 Nagisa Sekiguchi
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

import aquarius.expression.Action;
import aquarius.expression.AndPredict;
import aquarius.expression.Any;
import aquarius.expression.Capture;
import aquarius.expression.CharSet;
import aquarius.expression.Choice;
import aquarius.expression.CustomExpr;
import aquarius.expression.Literal;
import aquarius.expression.NotPredict;
import aquarius.expression.OneMore;
import aquarius.expression.Optional;
import aquarius.expression.PredictAction;
import aquarius.expression.Sequence;
import aquarius.expression.Sequence2;
import aquarius.expression.Sequence3;
import aquarius.expression.Sequence4;
import aquarius.expression.Sequence5;
import aquarius.expression.VoidCustomExpr;
import aquarius.expression.ZeroMore;

public interface ExpressionVisitor<T> {
	public          <R, A> T visitAction        (Action<R, A> expr);
	public                 T visitAndPredict    (AndPredict expr);
	public                 T visitAny           (Any expr);
	public                 T visitCapture       (Capture expr);
	public                 T visitCharSet       (CharSet expr);
	public             <R> T visitChoice        (Choice<R> expr);
	public             <R> T visitCustomExpr    (CustomExpr<R> expr);
	public                 T visitLiteral       (Literal expr);
	public                 T visitNotPredict    (NotPredict expr);
	public             <R> T visitOneMore       (OneMore<R> expr);
	public             <R> T visitOptional      (Optional<R> expr);
	public                 T visitPredictAction (PredictAction expr);
	public             <R> T visitRule          (Rule<R> expr);
	public                 T visitSequence      (Sequence expr);
	public          <A, B> T visitSequence2     (Sequence2<A, B> expr);
	public       <A, B, C> T visitSequence3     (Sequence3<A, B, C> expr);
	public    <A, B, C, D> T visitSequence4     (Sequence4<A, B, C, D> expr);
	public <A, B, C, D, E> T visitSequence5     (Sequence5<A, B, C, D, E> expr);
	public                 T visitVoidCustomExpr(VoidCustomExpr expr);
	public             <R> T visitZeroMore      (ZeroMore<R> expr);
}
