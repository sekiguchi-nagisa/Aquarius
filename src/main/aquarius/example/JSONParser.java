package aquarius.example;

import static aquarius.Expressions.*;
import static aquarius.example.JSON.*;

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
			.action((ctx, a) -> 
				a.get1()
			)
		);
	}

	@RuleDefinition
	public default Rule<JSONObject> object() {
		return rule(() ->
			choice(
				seq(objectOpen(), keyValue(), zeroMore(valueSep(), keyValue()), objectClose())
				.action((ctx, a) -> {
					JSONObject object = new JSONObject();
					for(Tuple2<Void, Tuple2<JSONString, JSON>> t : a.get2()) {
						object.add(t.get1());
					}
					return object;
				}),
				seq(objectOpen(), objectClose()).action((ctx, a) -> 
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
				.action((ctx, a) -> {
					JSONArray array = new JSONArray();
					for(Tuple2<Void, JSON> t : a.get2()) {
						array.add(t.get1());
					}
					return array;
				}),
				seq(arrayOpen(), arrayClose()).action((ctx, a) -> 
					new JSONArray()
				)
			)
		);
	}

	@RuleDefinition
	public default Rule<Tuple2<JSONString, JSON>> keyValue() {
		return rule(() ->
			seq(string(), keyValueSep(), value(), ws()).action((ctx, a) -> 
				new Tuple2<>(a.get0(), a.get2())
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
					str("true").action((ctx, a) -> new JSONBool(true)),
					str("false").action((ctx, a) -> new JSONBool(false)),
					str("null").action((ctx, a) -> new JSONNull())
				), 
				ws()
			).action((ctx, a) -> 
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
				str("\"")).action((ctx, a) -> 
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
				.action((ctx, a) -> 
					new JSONNumber(Double.parseDouble(ctx.createTokenText(a)))
				),
				$(str("-").opt(), integer()).action((ctx, a) -> 
					new JSONNumber(Long.parseLong(ctx.createTokenText(a))
				))
			)
		);
	}
}
