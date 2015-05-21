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

package aquarius.example;

import aquarius.Parser;
import aquarius.Rule;
import aquarius.Grammar;

import static aquarius.Expressions.*;

@Grammar
public interface SimpleJSONParser extends Parser {
    //separator definition

    default Rule<Void> objectOpen() {
        return () ->
                seqN(str("{"), WS);
    }

    default Rule<Void> objectClose() {
        return () ->
                seqN(str("}"), WS);
    }

    default Rule<Void> arrayOpen() {
        return () ->
                seqN(str("["), WS);
    }

    default Rule<Void> arrayClose() {
        return () ->
                seqN(str("]"), WS);
    }

    default Rule<Void> keyValueSep() {
        return () ->
                seqN(WS, str(":"), WS);
    }

    default Rule<Void> valueSep() {
        return () ->
                seqN(str(","), WS);
    }

    default Rule<Void> json() {
        return () ->
                seqN(WS, or(object(), array()));
    }

    default Rule<Void> object() {
        return () ->
                seqN(objectOpen(), keyValue(), zeroMore(valueSep(), keyValue()), objectClose())
                        .or(seqN(objectOpen(), objectClose()));
    }

    default Rule<Void> array() {
        return () ->
                seqN(arrayOpen(), value(), zeroMore(valueSep(), value()), arrayClose())
                        .or(seqN(arrayOpen(), arrayClose()));
    }

    default Rule<Void> keyValue() {
        return () ->
                seqN(string(), keyValueSep(), value(), WS);
    }

    default Rule<Void> value() {
        return () ->
                seqN(
                        or(
                                string(),
                                number(),
                                object(),
                                array(),
                                str("true"),
                                str("false"),
                                str("null")
                        ),
                        WS
                );
    }

    default Rule<Void> escape() {
        return () ->
                seqN(str("\\"), ch('"', '\\', '/', 'b', 'f', 'n', 'r', 't'));
    }

    default Rule<Void> string() {
        return () ->
                seqN(
                        str("\""),
                        or(
                                escape(),
                                seqN(not(ch('"', '\\')), ANY)
                        ).zeroMore(),
                        str("\"")
                );
    }

    default Rule<Void> integer() {
        return () ->
                str("0")
                        .or(seqN(r('1', '9'), r('0', '9').zeroMore()));
    }

    default Rule<Void> exp() {
        return () ->
                seqN(ch('E', 'e'), ch('+', '-').opt(), integer());
    }

    default Rule<Void> number() {
        return () ->
                seqN(str("-").opt(), integer(), str("."), r('0', '9').oneMore(), exp().opt())
                        .or(seqN(str("-").opt(), integer()));
    }
}
