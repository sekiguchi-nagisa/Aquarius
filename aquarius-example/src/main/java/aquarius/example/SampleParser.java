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
