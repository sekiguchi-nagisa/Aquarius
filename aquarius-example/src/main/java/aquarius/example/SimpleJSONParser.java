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

import aquarius.Parser;
import aquarius.Rule;
import aquarius.annotation.Grammar;
import aquarius.annotation.RuleDefinition;

@Grammar
public interface SimpleJSONParser extends Parser {
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
	public default Rule<Void> json() {
		return () -> 
			seqN(WS, or(object(), array()));
	}

	@RuleDefinition
	public default Rule<Void> object() {
		return () ->
			seqN(objectOpen(), keyValue(), zeroMore(valueSep(), keyValue()), objectClose())
			.or(seqN(objectOpen(), objectClose()));
	}

	@RuleDefinition
	public default Rule<Void> array() {
		return () ->
			seqN(arrayOpen(), value(), zeroMore(valueSep(), value()), arrayClose())
			.or(seqN(arrayOpen(), arrayClose()));
	}

	@RuleDefinition
	public default Rule<Void> keyValue() {
		return () ->
			seqN(string(), keyValueSep(), value(), WS);
	}

	@RuleDefinition
	public default Rule<Void> value() {
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

	@RuleDefinition
	public default Rule<Void> escape() {
		return () ->
			seqN(str("\\"), ch('"', '\\', '/', 'b', 'f', 'n', 'r', 't'));
	}

	@RuleDefinition
	public default Rule<Void> string() {
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
	public default Rule<Void> number() {
		return () ->
			seqN(str("-").opt(), integer(), str("."), r('0', '9').oneMore(), exp().opt())
			.or(seqN(str("-").opt(), integer()));
	}
}
