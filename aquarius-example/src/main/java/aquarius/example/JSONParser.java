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
import aquarius.misc.Tuple2;

import static aquarius.Expressions.*;
import static aquarius.example.JSON.*;
import static aquarius.misc.Tuples.of;

@Grammar
public interface JSONParser extends Parser {
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

    default Rule<JSON> json() {
        return () ->
                seq(WS, or(object(), array())).map((ctx, a) ->
                                a.get1()
                );
    }

    default Rule<JSONObject> object() {
        return () ->
                or(
                        seq(objectOpen(), keyValue(), zeroMore(valueSep(), keyValue()), objectClose())
                        .map((ctx, a) -> {
                            JSONObject object = new JSONObject();
                            object.add(a.get1());
                            for(Tuple2<Void, Tuple2<JSONString, JSON>> t : a.get2()) {
                                object.add(t.get1());
                            }
                            return object;
                        }),
                        seq(objectOpen(), objectClose()).map((ctx, a) ->
                                        new JSONObject()
                        )
                );
    }

    default Rule<JSONArray> array() {
        return () ->
                or(
                        seq(arrayOpen(), value(), zeroMore(valueSep(), value()), arrayClose())
                        .map((ctx, a) -> {
                            JSONArray array = new JSONArray();
                            array.add(a.get1());
                            for(Tuple2<Void, JSON> t : a.get2()) {
                                array.add(t.get1());
                            }
                            return array;
                        }),
                        seq(arrayOpen(), arrayClose()).map((ctx, a) ->
                                        new JSONArray()
                        )
                );
    }

    default Rule<Tuple2<JSONString, JSON>> keyValue() {
        return () ->
                seq(string(), keyValueSep(), value(), WS).map((ctx, a) ->
                                of(a.get0(), a.get2())
                );
    }

    default Rule<JSON> value() {
        return () ->
                seq(
                        or(
                                string(),
                                number(),
                                object(),
                                array(),
                                str("true").map((ctx, a) -> new JSONBool(true)),
                                str("false").map((ctx, a) -> new JSONBool(false)),
                                str("null").map((ctx, a) -> new JSONNull())
                        ),
                        WS
                ).filter0();
    }

    default Rule<Void> escape() {
        return () ->
                seqN(str("\\"), ch('"', '\\', '/', 'b', 'f', 'n', 'r', 't'));
    }

    default Rule<JSONString> string() {
        return () ->
                $(
                        str("\""),
                        or(
                                escape(),
                                seqN(not(ch('"', '\\')), ANY)
                        ).zeroMore(),
                        str("\"")
                ).map((ctx, a) ->
                                new JSONString(ctx.createTokenText(a))
                );
    }

    default Rule<Void> integer() {
        return () ->
                or(
                        str("0"),
                        seqN(r('1', '9'), r('0', '9').zeroMore())
                );
    }

    default Rule<Void> exp() {
        return () ->
                seqN(ch('E', 'e'), ch('+', '-').opt(), integer());
    }

    default Rule<JSONNumber> number() {
        return () ->
                or(
                        $(str("-").opt(), integer(), str("."), r('0', '9').oneMore(), exp().opt())
                        .map((ctx, a) ->
                                        new JSONNumber(Double.parseDouble(ctx.createTokenText(a)))
                        ),
                        $(str("-").opt(), integer()).map((ctx, a) ->
                                        new JSONNumber(Long.parseLong(ctx.createTokenText(a)))
                        )
                );
    }
}
