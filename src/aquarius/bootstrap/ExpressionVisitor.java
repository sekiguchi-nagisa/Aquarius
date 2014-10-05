package aquarius.bootstrap;

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
