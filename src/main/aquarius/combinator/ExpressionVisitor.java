package aquarius.combinator;

import aquarius.combinator.expression.Action;
import aquarius.combinator.expression.AndPredict;
import aquarius.combinator.expression.AndPredictAction;
import aquarius.combinator.expression.Any;
import aquarius.combinator.expression.Capture;
import aquarius.combinator.expression.CharSet;
import aquarius.combinator.expression.Choice;
import aquarius.combinator.expression.NotPredict;
import aquarius.combinator.expression.NotPredictAction;
import aquarius.combinator.expression.OneMore;
import aquarius.combinator.expression.Optional;
import aquarius.combinator.expression.Rule;
import aquarius.combinator.expression.Sequence;
import aquarius.combinator.expression.Literal;
import aquarius.combinator.expression.Sequence2;
import aquarius.combinator.expression.Sequence3;
import aquarius.combinator.expression.Sequence4;
import aquarius.combinator.expression.Sequence5;
import aquarius.combinator.expression.ZeroMore;

public interface ExpressionVisitor<T> {
	public                 T visitLiteral         (Literal expr);
	public                 T visitAny             (Any expr);
	public                 T visitCharSet         (CharSet expr);
	public             <R> T visitRule            (Rule<R> expr);
	public             <R> T visitZeroMore        (ZeroMore<R> expr);
	public             <R> T visitOneMore         (OneMore<R> expr);
	public             <R> T visitOptional        (Optional<R> expr);
	public                 T visitAndPredict      (AndPredict expr);
	public                 T visitNotPredict      (NotPredict expr);
	public             <R> T visitSequence        (Sequence<R> expr);
	public          <A, B> T visitSequence2       (Sequence2<A, B> expr);
	public       <A, B, C> T visitSequence3       (Sequence3<A, B, C> expr);
	public    <A, B, C, D> T visitSequence4       (Sequence4<A, B, C, D> expr);
	public <A, B, C, D, E> T visitSequence5       (Sequence5<A, B, C, D, E> expr);
	public             <R> T visitChoice          (Choice<R> expr);
	public          <R, A> T visitAction          (Action<R, A> expr);
	public             <A> T visitAndPredictAction(AndPredictAction<A> expr);
	public             <A> T visitNotPredictAction(NotPredictAction<A> expr);
	public                 T visitCapture         (Capture expr);
}
