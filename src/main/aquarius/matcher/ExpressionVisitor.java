package aquarius.matcher;

import aquarius.matcher.Grammar.Rule;
import aquarius.matcher.expression.AndPredict;
import aquarius.matcher.expression.NoArgAction;
import aquarius.matcher.expression.PredictAction;
import aquarius.matcher.expression.Any;
import aquarius.matcher.expression.Capture;
import aquarius.matcher.expression.CharSet;
import aquarius.matcher.expression.Choice;
import aquarius.matcher.expression.Literal;
import aquarius.matcher.expression.Action;
import aquarius.matcher.expression.NotPredict;
import aquarius.matcher.expression.OneMore;
import aquarius.matcher.expression.Optional;
import aquarius.matcher.expression.Sequence;
import aquarius.matcher.expression.Sequence2;
import aquarius.matcher.expression.Sequence3;
import aquarius.matcher.expression.Sequence4;
import aquarius.matcher.expression.Sequence5;
import aquarius.matcher.expression.ZeroMore;

public interface ExpressionVisitor<T> {
	public          <R, A> T visitAction        (Action<R, A> expr);
	public                 T visitAndPredict    (AndPredict expr);
	public                 T visitAny           (Any expr);
	public                 T visitCapture       (Capture expr);
	public                 T visitCharSet       (CharSet expr);
	public             <R> T visitChoice        (Choice<R> expr);
	public                 T visitLiteral       (Literal expr);
	public             <R> T visitNoArgAction   (NoArgAction<R> expr);
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
	public             <R> T visitZeroMore      (ZeroMore<R> expr);
}
