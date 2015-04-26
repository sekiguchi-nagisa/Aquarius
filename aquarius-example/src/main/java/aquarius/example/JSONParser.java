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

import static aquarius.Expressions.*;
import static aquarius.example.JSON.*;
import static aquarius.misc.Tuples.*;

import aquarius.Parser;
import aquarius.Rule;
import aquarius.annotation.Grammar;
import aquarius.annotation.RuleDefinition;
import aquarius.misc.Tuple2;

@Grammar
public interface JSONParser extends Parser {
	//separator definition
	
	@RuleDefinition
	public default Rule<Void> objectOpen() {
		return () ->
			seqN(str("{"), WS);
	}

	@RuleDefinition
	public default Rule<Void> objectClose() {
		return () ->
			seqN(str("}"), WS);
	}

	@RuleDefinition
	public default Rule<Void> arrayOpen() {
		return () ->
			seqN(str("["), WS);
	}

	@RuleDefinition
	public default Rule<Void> arrayClose() {
		return () ->
			seqN(str("]"), WS);
	}

	@RuleDefinition
	public default Rule<Void> keyValueSep() {
		return () ->
			seqN(WS, str(":"), WS);
	}

	@RuleDefinition
	public default Rule<Void> valueSep() {
		return () ->
			seqN(str(","), WS);
	}

	@RuleDefinition
	public default Rule<JSON> json() {
		return () -> 
			seq(WS, or(object(), array())).map((ctx, a) -> 
				a.get1()
			);
	}

	@RuleDefinition
	public default Rule<JSONObject> object() {
		return () ->
			seq(objectOpen(), keyValue(), zeroMore(valueSep(), keyValue()), objectClose())
			.map((ctx, a) -> {
				JSONObject object = new JSONObject();
				object.add(a.get1());
				for(Tuple2<Void, Tuple2<JSONString, JSON>> t : a.get2()) {
					object.add(t.get1());
				}
				return object;
			})
			.or(seq(objectOpen(), objectClose()).map((ctx, a) -> 
				new JSONObject()
			));
	}

	@RuleDefinition
	public default Rule<JSONArray> array() {
		return () ->
			seq(arrayOpen(), value(), zeroMore(valueSep(), value()), arrayClose())
			.map((ctx, a) -> {
				JSONArray array = new JSONArray();
				array.add(a.get1());
				for(Tuple2<Void, JSON> t : a.get2()) {
					array.add(t.get1());
				}
				return array;
			})
			.or(seq(arrayOpen(), arrayClose()).map((ctx, a) -> 
				new JSONArray()
			));
	}

	@RuleDefinition
	public default Rule<Tuple2<JSONString, JSON>> keyValue() {
		return () ->
			seq(string(), keyValueSep(), value(), WS).map((ctx, a) -> 
				of(a.get0(), a.get2())
			);
	}

	@RuleDefinition
	public default Rule<JSON> value() {
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

	@RuleDefinition
	public default Rule<Void> escape() {
		return () ->
			seqN(str("\\"), ch('"', '\\', '/', 'b', 'f', 'n', 'r', 't'));
	}

	@RuleDefinition
	public default Rule<JSONString> string() {
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

	@RuleDefinition
	public default Rule<Void> integer() {
		return () ->
			str("0")
			.or(seqN(r('1', '9'), r('0', '9').zeroMore()));
	}

	@RuleDefinition
	public default Rule<Void> exp() {
		return () ->
			seqN(ch('E', 'e'), ch('+', '-').opt(), integer());
	}

	@RuleDefinition
	public default Rule<JSONNumber> number() {
		return () ->
			$(str("-").opt(), integer(), str("."), r('0', '9').oneMore(), exp().opt())
			.map((ctx, a) -> 
				new JSONNumber(Double.parseDouble(ctx.createTokenText(a)))
			)
			.or($(str("-").opt(), integer()).map((ctx, a) -> 
				new JSONNumber(Long.parseLong(ctx.createTokenText(a)))
			));
	}
}
