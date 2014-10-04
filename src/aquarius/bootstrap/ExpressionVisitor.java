package aquarius.bootstrap;

public interface ExpressionVisitor<T> {
	public T visitString          (string expr);
	public T visitAny             (any expr);
	public T visitCharSet         (charSet expr);
	public T visitRule            (rule expr);
	public T visitSubExpr         (subExpr expr);
	public T visitZeroMore        (zeroMore expr);
	public T visitOneMore         (oneMore expr);
	public T visitOptional        (optional expr);
	public T visitAndPredict      (andPredict expr);
	public T visitNotPredict      (notPredict expr);
	public T visitSeq             (seq expr);
	public T visitChoice          (choice expr);
	public T visitAction          (action expr);
	public T visitAndPredictAction(andPredictAction expr);
	public T visitNotPredictAction(notPredictAction expr);
	public T visitCapture         (capture expr);
}
