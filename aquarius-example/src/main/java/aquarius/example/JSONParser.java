package aquarius.example;

import static aquarius.Expressions.*;
import static aquarius.example.JSON.*;
import static aquarius.misc.Tuples.*;

import java.util.List;
import java.util.Optional;

import aquarius.Parser;
import aquarius.Rule;
import aquarius.annotation.Grammar;
import aquarius.annotation.RuleDefinition;
import aquarius.misc.Tuple2;
import aquarius.misc.Tuple3;

@Grammar
public interface JSONParser extends Parser {
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
	public default Rule<JSON> json() {
		return rule(() -> 
			seq(ws(), choice(object(), array()))
			.map((ctx, a) -> 
				a.get1()
			)
		);
	}

	@RuleDefinition
	public default Rule<JSONObject> object() {
		return rule(() ->
			choice(
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
			)
		);
	}

	@RuleDefinition
	public default Rule<JSONArray> array() {
		return rule(() ->
			choice(
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
			)
		);
	}

	@RuleDefinition
	public default Rule<Tuple2<JSONString, JSON>> keyValue() {
		return rule(() ->
			seq(string(), keyValueSep(), value(), ws()).map((ctx, a) -> 
				of(a.get0(), a.get2())
			)
		);
	}

	@RuleDefinition
	public default Rule<JSON> value() {
		return rule(() ->
			seq(
				choice(
					string(),
					number(),
					object(),
					array(),
					str("true").map((ctx, a) -> new JSONBool(true)),
					str("false").map((ctx, a) -> new JSONBool(false)),
					str("null").map((ctx, a) -> new JSONNull())
				), 
				ws()
			).map((ctx, a) -> 
				a.get0()
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
	public default Rule<JSONString> string() {
		return rule(() ->
			$(
				str("\""), 
				choice(
					escape(), 
					seqN(not(ch('"', '\\')), ANY)
				).zeroMore(), 
				str("\"")).map((ctx, a) -> 
					new JSONString(ctx.createTokenText(a)
				)
			)
		);
	}

	@RuleDefinition
	public default Rule<Void> integer() {
		return ruleVoid(() ->
			choice(
				str("0"),
				seqN(r('1', '9'), r('0', '9').zeroMore())
			)
		);
	}

	@RuleDefinition
	public default Rule<Tuple3<Void, Optional<Void>, Void>> exp() {
		return rule(() ->
			seq(ch('E', 'e'), ch('+', '-').opt(), integer())
		);
	}

	@RuleDefinition
	public default Rule<JSONNumber> number() {
		return rule(() ->
			choice(
				$(str("-").opt(), integer(), str("."), r('0', '9').oneMore(), exp().opt())
				.map((ctx, a) -> 
					new JSONNumber(Double.parseDouble(ctx.createTokenText(a)))
				),
				$(str("-").opt(), integer()).map((ctx, a) -> 
					new JSONNumber(Long.parseLong(ctx.createTokenText(a))
				))
			)
		);
	}
}
