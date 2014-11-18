package aquarius.example;

import static aquarius.matcher.Expressions.$;
import static aquarius.matcher.Expressions.ANY;
import static aquarius.matcher.Expressions.ch;
import static aquarius.matcher.Expressions.choice;
import static aquarius.matcher.Expressions.not;
import static aquarius.matcher.Expressions.r;
import static aquarius.matcher.Expressions.seq;
import static aquarius.matcher.Expressions.str;

import java.util.List;

import aquarius.matcher.NoneTerminal;
import aquarius.matcher.Parser;
import aquarius.runtime.Token;
import aquarius.runtime.annotation.RuleDefinition;

public @aquarius.runtime.annotation.Grammar
interface Sample2 extends Parser {

	@RuleDefinition
	public default NoneTerminal<Void> EOF() {
		return rule(() ->
			not(ANY)
		);
	}

	@RuleDefinition
	public default NoneTerminal<List<Void>> __() {
		return rule(() -> 
			ch(' ', '\t', '\n', '\r').zeroMore()
		);
	}

	@RuleDefinition
	public default NoneTerminal<Token> Expr() {
		return rule(() -> 
			seq(__(), Add(), __()).action((ctx, a) -> a.get1())
		);
	}

	@RuleDefinition
	public default NoneTerminal<Token> Add() {
		return rule(() ->
			choice(
				$(Mul(), __(), str("+"), __(), Add()),
				$(Mul(), __(), str("-"), __(), Add()),
				Mul()
			)
		);
	}

	@RuleDefinition
	public default NoneTerminal<Token> Mul() {
		return rule(() -> 
			choice(
				$(Primary(), __(), str("*"), __(), Mul()),
				$(Primary(), __(), str("/"), __(), Mul()),
				Primary()
			)
		);
	}

	@RuleDefinition
	public default NoneTerminal<Token> Primary() {
		return rule(() ->
			choice(
				$(str("("), __(), Add(), __(), str(")")),
				Num()
			)
		);
	}

	@RuleDefinition
	public default NoneTerminal<Token> Num() {
		return rule(() -> 
			choice(
				$(str("0")),
				$(ch('-', '+').opt(), r('1', '9'), r('0', '9').zeroMore())
			)
		);
	}
}
