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

import aquarius.annotation.Grammar;
import aquarius.annotation.RuleDefinition;
import aquarius.Rule;
import aquarius.Parser;
import aquarius.Token;

@Grammar
public interface SampleParser extends Parser {

	@RuleDefinition
	public default Rule<Void> EOF() {
		return ruleVoid(() ->
			not(ANY)
		);
	}

	@RuleDefinition
	public default Rule<List<Void>> __() {
		return rule(() -> 
			ch(' ', '\t', '\n', '\r').zeroMore()
		);
	}

	@RuleDefinition
	public default Rule<Token> Expr() {
		return rule(() -> 
			seq(__(), Add(), __()).map((ctx, a) -> a.get1())
		);
	}

	@RuleDefinition
	public default Rule<Token> Add() {
		return rule(() ->
			$(Mul(), __(), str("+"), __(), Add())
			.or($(Mul(), __(), str("-"), __(), Add()))
			.or(Mul())
		);
	}

	@RuleDefinition
	public default Rule<Token> Mul() {
		return rule(() -> 
			$(Primary(), __(), str("*"), __(), Mul())
			.or($(Primary(), __(), str("/"), __(), Mul()))
			.or(Primary())
		);
	}

	@RuleDefinition
	public default Rule<Token> Primary() {
		return rule(() ->
			$(str("("), __(), Add(), __(), str(")"))
			.or(Num())
		);
	}

	@RuleDefinition
	public default Rule<Token> Num() {
		return rule(() -> 
			$(str("0"))
			.or($(ch('-', '+').opt(), r('1', '9'), r('0', '9').zeroMore()))
		);
	}
}
