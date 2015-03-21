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

import java.util.ArrayList;
import java.util.List;

import aquarius.action.FailedActionException;
import aquarius.expression.AndPredict;
import aquarius.expression.Any;
import aquarius.expression.CharSet;
import aquarius.expression.Literal;
import aquarius.expression.NotPredict;
import aquarius.expression.ParsingExpression;
import aquarius.expression.PredictAction;
import aquarius.misc.TypeMatch;
import aquarius.misc.Utf8Util;

public class Failure {
	private int failurePos = -1;
	private Object cause;

	public int getFailurePos() {
		return this.failurePos;
	}

	public void reuse(int failurePos, ParsingExpression<?> expr) {
		this.failurePos = failurePos;
		this.cause = expr;
	}

	public void reuse(int failurePos, FailedActionException e) {
		this.failurePos = failurePos;
		this.cause = e;
	}

	public String getMessage(AquariusInputStream input) {
		StringBuilder sBuilder = new StringBuilder();
		appendFailurePos(sBuilder, input, this.getFailurePos());

		TypeMatch.match(this.cause)
			.when(FailedActionException.class, a -> 
				sBuilder.append(a.getMessage())
			)
			.when(Any.class, a -> 
				sBuilder.append("require any character, but reach End of File")
			)
			.when(CharSet.class, a -> {
				if(isEnfOfInput(input, this.getFailurePos())) {
					sBuilder.append("require chars: " + a + ", but reach End of File");
				} else {
					int curPos = input.getPosition();

					input.setPosition(failurePos);
					sBuilder.append("require chars: " + a);
					sBuilder.append(", but is: " + Utf8Util.codeToString(input.fetch()));

					input.setPosition(curPos);
				}
			})
			.when(Literal.class, a -> {
				if(isEnfOfInput(input, this.getFailurePos())) {
					sBuilder.append("require text: " + a.getTarget() + ", but reach End of File");
				} else {
					int curPos = input.getPosition();

					sBuilder.append("require text: " + a.getTarget());
					int size = a.getTargetCodes().length;
					List<Integer> codeList = new ArrayList<>(size);
					for(int i = 0; i < size && input.fetch() != AquariusInputStream.EOF; i++) {
						codeList.add(input.fetch());
						input.consume();
					}
					sBuilder.append(", but is: " + Utf8Util.codeListToString(codeList));

					input.setPosition(curPos);
				}
			})
			.when(AndPredict.class, a -> 
				sBuilder.append("failed And Prediction")
			)
			.when(NotPredict.class, a -> 
				sBuilder.append("failed Not Prediction")
			)
			.when(PredictAction.class, a ->
				sBuilder.append("failed And Prediction")
			)
			.orElse(a -> {
				if(a.isPresent()) {
					throw new RuntimeException("unsupported class: " + a.get().getClass());
				} else {
					throw new RuntimeException("must not null value");
				}
			});
		appendFialureLine(sBuilder, input, this.getFailurePos());
		return sBuilder.toString();
	}

	private static boolean isEnfOfInput(AquariusInputStream input, int pos) {
		int curPos = input.getPosition();
		input.setPosition(pos);
		try {
			return input.fetch() == AquariusInputStream.EOF;
		} finally {
			input.setPosition(curPos);
		}
	}

	private static void appendFailurePos(StringBuilder sBuilder, AquariusInputStream input, int pos) {
		Token token = input.createToken(pos, 0);
		sBuilder.append('(');
		sBuilder.append(input.getSourceName());
		sBuilder.append(')');
		sBuilder.append(':');
		sBuilder.append(token.getLineNumber(input));
		sBuilder.append(':');
		sBuilder.append(token.getCodePosInLine(input));
		sBuilder.append(": ");
	}

	private static void appendFialureLine(StringBuilder sBuilder, AquariusInputStream input, int pos) {
		int curPos = input.getPosition();

		// create line
		final int lineStartPos = input.createToken(pos, 0).getLineStartPos(input);
		input.setPosition(pos);
		input.consume();
		String line = input.createToken(lineStartPos).getText(input);
		sBuilder.append(System.lineSeparator());
		sBuilder.append(line);
		sBuilder.append(System.lineSeparator());

		// create line marker
		input.setPosition(lineStartPos);
		int startPos = lineStartPos;
		while(startPos < pos) {
			sBuilder.append(' ');
			if(!Utf8Util.isAsciiCode(input.fetch())) {
				sBuilder.append(' ');
			}
			input.consume();
			startPos = input.getPosition();
		}
		sBuilder.append('^');

		input.setPosition(curPos);
	}
}
