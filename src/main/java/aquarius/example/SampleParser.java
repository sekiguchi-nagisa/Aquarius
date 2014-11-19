package aquarius.example;

import static aquarius.Expressions.$;
import static aquarius.Expressions.ANY;
import static aquarius.Expressions.ch;
import static aquarius.Expressions.choice;
import static aquarius.Expressions.not;
import static aquarius.Expressions.r;
import static aquarius.Expressions.seq;
import static aquarius.Expressions.str;

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
			seq(__(), Add(), __()).action((ctx, a) -> a.get1())
		);
	}

	@RuleDefinition
	public default Rule<Token> Add() {
		return rule(() ->
			choice(
				$(Mul(), __(), str("+"), __(), Add()),
				$(Mul(), __(), str("-"), __(), Add()),
				Mul()
			)
		);
	}

	@RuleDefinition
	public default Rule<Token> Mul() {
		return rule(() -> 
			choice(
				$(Primary(), __(), str("*"), __(), Mul()),
				$(Primary(), __(), str("/"), __(), Mul()),
				Primary()
			)
		);
	}

	@RuleDefinition
	public default Rule<Token> Primary() {
		return rule(() ->
			choice(
				$(str("("), __(), Add(), __(), str(")")),
				Num()
			)
		);
	}

	@RuleDefinition
	public default Rule<Token> Num() {
		return rule(() -> 
			choice(
				$(str("0")),
				$(ch('-', '+').opt(), r('1', '9'), r('0', '9').zeroMore())
			)
		);
	}
}
