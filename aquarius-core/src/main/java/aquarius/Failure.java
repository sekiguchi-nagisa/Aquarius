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

import aquarius.action.FailedActionException;
import aquarius.expression.*;
import aquarius.misc.TypeMatch;
import aquarius.misc.Utf8Util;

import java.util.ArrayList;
import java.util.List;

public class Failure {
    private int failurePos = -1;
    private Object cause;

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
        sBuilder.append(input.getLineNumber(token));
        sBuilder.append(':');
        sBuilder.append(input.getCodePosInLine(token));
        sBuilder.append(": ");
    }

    private static void appendFailureLine(StringBuilder sBuilder, AquariusInputStream input, int pos) {
        int curPos = input.getPosition();

        // create line
        final int lineStartPos = input.getLineStartPos(input.createToken(pos, 0));
        input.setPosition(pos);
        input.consume();
        String line = input.getTokenText(input.createToken(lineStartPos));
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
                        sBuilder.append("require chars: ").append(a).append(", but reach End of File");
                    } else {
                        int curPos = input.getPosition();

                        input.setPosition(failurePos);
                        sBuilder.append("require chars: ").append(a);
                        sBuilder.append(", but is: ").append(Utf8Util.codeToString(input.fetch()));

                        input.setPosition(curPos);
                    }
                })
                .when(Literal.class, a -> {
                    if(isEnfOfInput(input, this.getFailurePos())) {
                        sBuilder.append("require text: ").append(a.getTarget()).append(", but reach End of File");
                    } else {
                        int curPos = input.getPosition();

                        sBuilder.append("require text: ").append(a.getTarget());
                        int size = a.getTargetCodes().length;
                        List<Integer> codeList = new ArrayList<>(size);
                        for(int i = 0; i < size && input.fetch() != AquariusInputStream.EOF; i++) {
                            codeList.add(input.fetch());
                            input.consume();
                        }
                        sBuilder.append(", but is: ").append(Utf8Util.codeListToString(codeList));

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
        appendFailureLine(sBuilder, input, this.getFailurePos());
        return sBuilder.toString();
    }
}
