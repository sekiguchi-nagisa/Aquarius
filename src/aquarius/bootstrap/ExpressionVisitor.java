package aquarius.bootstrap;

import aquarius.bootstrap.Pattern.action;
import aquarius.bootstrap.Pattern.andPredict;
import aquarius.bootstrap.Pattern.andPredictAction;
import aquarius.bootstrap.Pattern.any;
import aquarius.bootstrap.Pattern.capture;
import aquarius.bootstrap.Pattern.charSet;
import aquarius.bootstrap.Pattern.choice;
import aquarius.bootstrap.Pattern.notPredict;
import aquarius.bootstrap.Pattern.notPredictAction;
import aquarius.bootstrap.Pattern.oneMore;
import aquarius.bootstrap.Pattern.optional;
import aquarius.bootstrap.Pattern.rule;
import aquarius.bootstrap.Pattern.seq;
import aquarius.bootstrap.Pattern.string;
import aquarius.bootstrap.Pattern.subExpr;
import aquarius.bootstrap.Pattern.zeroMore;

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
