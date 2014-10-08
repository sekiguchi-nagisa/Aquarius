package aquarius.runtime;

import aquarius.runtime.expression.Action;
import aquarius.runtime.expression.AndPredict;
import aquarius.runtime.expression.AndPredictAction;
import aquarius.runtime.expression.Any;
import aquarius.runtime.expression.Capture;
import aquarius.runtime.expression.CharSet;
import aquarius.runtime.expression.Choice;
import aquarius.runtime.expression.NotPredict;
import aquarius.runtime.expression.NotPredictAction;
import aquarius.runtime.expression.OneMore;
import aquarius.runtime.expression.Optional;
import aquarius.runtime.expression.Rule;
import aquarius.runtime.expression.Sequence;
import aquarius.runtime.expression.StringLiteral;
import aquarius.runtime.expression.SubExpr;
import aquarius.runtime.expression.ZeroMore;

public interface ExpressionVisitor<T> {
	public T visitString          (StringLiteral expr);
	public T visitAny             (Any expr);
	public T visitCharSet         (CharSet expr);
	public T visitRule            (Rule expr);
	public T visitSubExpr         (SubExpr expr);
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
