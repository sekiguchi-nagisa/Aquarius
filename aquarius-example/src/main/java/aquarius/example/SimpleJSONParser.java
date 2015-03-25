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

import java.util.List;

import aquarius.Parser;
import aquarius.Rule;
import aquarius.annotation.Grammar;
import aquarius.annotation.RuleDefinition;

@Grammar
public interface SimpleJSONParser extends Parser {
	//separator definition

	@RuleDefinition
	public default Rule<List<Void>> ws() {
		return rule(() ->
			ch(' ', '\t', '\r', '\n').zeroMore()
		);
	}

	@RuleDefinition
	public default Rule<Void> objectOpen() {
		return ruleVoid(() ->
			seqN(str("{"), ws())
		);
	}

	@RuleDefinition
	public default Rule<Void> objectClose() {
		return ruleVoid(() ->
			seqN(str("}"), ws())
		);
	}

	@RuleDefinition
	public default Rule<Void> arrayOpen() {
		return ruleVoid(() ->
			seqN(str("["), ws())
		);
	}

	@RuleDefinition
	public default Rule<Void> arrayClose() {
		return ruleVoid(() ->
			seqN(str("]"), ws())
		);
	}

	@RuleDefinition
	public default Rule<Void> keyValueSep() {
		return ruleVoid(() ->
			seqN(ws(), str(":"), ws())
		);
	}

	@RuleDefinition
	public default Rule<Void> valueSep() {
		return ruleVoid(() ->
			seqN(str(","), ws())
		);
	}

	@RuleDefinition
	public default Rule<Void> json() {
		return rule(() -> 
			seqN(ws(), or(object(), array()))
		);
	}

	@RuleDefinition
	public default Rule<Void> object() {
		return rule(() ->
			seqN(objectOpen(), keyValue(), zeroMore(valueSep(), keyValue()), objectClose())
			.or(seqN(objectOpen(), objectClose()))
		);
	}

	@RuleDefinition
	public default Rule<Void> array() {
		return rule(() ->
			seqN(arrayOpen(), value(), zeroMore(valueSep(), value()), arrayClose())
			.or(seqN(arrayOpen(), arrayClose()))
		);
	}

	@RuleDefinition
	public default Rule<Void> keyValue() {
		return rule(() ->
			seqN(string(), keyValueSep(), value(), ws())
		);
	}

	@RuleDefinition
	public default Rule<Void> value() {
		return rule(() ->
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
				ws()
			)
		);
	}

	@RuleDefinition
	public default Rule<Void> escape() {
		return ruleVoid(() ->
			seqN(str("\\"), ch('"', '\\', '/', 'b', 'f', 'n', 'r', 't'))
		);
	}

	@RuleDefinition
	public default Rule<Void> string() {
		return rule(() ->
			seqN(
				str("\""), 
				or(
					escape(), 
					seqN(not(ch('"', '\\')), ANY)
				).zeroMore(), 
				str("\"")
			)
		);
	}

	@RuleDefinition
	public default Rule<Void> integer() {
		return ruleVoid(() ->
			str("0")
			.or(seqN(r('1', '9'), r('0', '9').zeroMore()))
		);
	}

	@RuleDefinition
	public default Rule<Void> exp() {
		return ruleVoid(() ->
			seqN(ch('E', 'e'), ch('+', '-').opt(), integer())
		);
	}

	@RuleDefinition
	public default Rule<Void> number() {
		return rule(() ->
			seqN(str("-").opt(), integer(), str("."), r('0', '9').oneMore(), exp().opt())
			.or(seqN(str("-").opt(), integer()))
		);
	}
}
