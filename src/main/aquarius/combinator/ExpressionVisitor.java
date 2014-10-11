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
import aquarius.combinator.expression.ZeroMore;

public interface ExpressionVisitor<T> {
	public T visitLiteral         (Literal expr);
	public T visitAny             (Any expr);
	public T visitCharSet         (CharSet expr);
	public T visitRule            (Rule expr);
	public T visitZeroMore        (ZeroMore expr);
	public T visitOneMore         (OneMore expr);
	public T visitOptional        (Optional expr);
	public T visitAndPredict      (AndPredict expr);
	public T visitNotPredict      (NotPredict expr);
	public T visitSeq             (Sequence expr);
	public T visitChoice          (Choice expr);
	public T visitAction          (Action expr);
	public T visitAndPredictAction(AndPredictAction expr);
	public T visitNotPredictAction(NotPredictAction expr);
	public T visitCapture         (Capture expr);
}
