grammar aquarius;

@header {
package aquarius.bootstrap;
import static aquarius.bootstrap.ParsingExpression.*;
}


grammarDefinition
	: ruleDefinition+ EOF
	;

ruleDefinition
	: RuleName '=' expression RuleEnd
	;

expression
	: choice
	;

choice
	: action ('/' action)*
	;

action
	: sequence ('{' Number '}')?
	;

sequence
	: (prediction)+
	;

prediction
	: predictionAction
	| ('&' | '!')? suffixExpr 
	;

predictionAction
	: ('&' | '!') '{' Number '}'
	;

suffixExpr
	: primary ('*' | '+' | '?')?
	;

primary returns [ParsingExpression expr]
	: String
	| Any { $expr = any();}
	| CharSet
	| RuleName
	| '(' expression ')'
	| '<' expression '>'
	;


RuleName
	: [_a-zA-Z][_a-zA-Z0-9]*
	;

Number
	: '0'
	| [1-9][0-9]*
	;

String
	: '\'' StringChar* '\''
	;

fragment
StringChar
	: ~[\r\n'\\]
	| EscapeSequence
	;
	
EscapeSequence
	: '\\' [btnfr'\\]
	;

Any
	: '.'
	;

CharSet
	: '[' CharSetContent ']'
	;

fragment
CharSetContent
	: (~[\]\\] | '\\' .)+
	;

RuleEnd
	: ';'
	;

Comment
	: '//' ~[\r\n]* -> skip
	;

WhiteSpace
	: [ \t\r\n] -> skip
	;