package aquarius.example;

import static aquarius.matcher.Expressions.*;
import static aquarius.example.JSON.*;

import java.util.List;
import java.util.Optional;

import aquarius.matcher.Grammar;
import aquarius.misc.Tuple2;
import aquarius.misc.Tuple3;

public class JSONGrammar extends Grammar {
	public final Rule<JSON> json = rule("json");
	private final Rule<JSONObject> object = rule("object");
	private final Rule<JSONArray> array = rule("array");
	private final Rule<Tuple2<JSONString, JSON>> keyValue = rule("keyValue");
	private final Rule<JSON> value = rule("value");
	private final Rule<JSONString> string = rule("string");
	private final Rule<JSONNumber> number = rule("number");

	public JSONGrammar() {
		//separator definition
		Rule<List<Void>> ws = rule("ws", 
			ch(' ', '\t', '\r', '\n').zeroMore()
		);

		Rule<Void> objectOpen = ruleVoid("objectOpen",
			seqN(str("{"), ws)
		);

		Rule<Void> objectClose = ruleVoid("objectClose",
			seqN(str("}"), ws)
		);

		Rule<Void> arrayOpen = ruleVoid("arrayOpen",
			seqN(str("["), ws)
		);

		Rule<Void> arrayClose = ruleVoid("arrayClose",
			seqN(str("]"), ws)
		);

		Rule<Void> keyValueSep = ruleVoid("keyValueSep",
			seqN(ws, str(":"), ws)
		);

		Rule<Void> valueSep = ruleVoid("valueSep",
			seqN(str(","), ws)
		);

		// json definition
		def(json, 
			seq(ws, choice(object, array))
				.action((ctx, a) -> 
					a.get1()
				)
		);

		def(object, 
			choice(
				seq(objectOpen, keyValue, zeroMore(valueSep, keyValue), objectClose)
					.action((ctx, a) -> {
						JSONObject object = new JSONObject();
						for(Tuple2<Void, Tuple2<JSONString, JSON>> t : a.get2()) {
							object.add(t.get1());
						}
						return object;
					}),
				seq(objectOpen, objectClose)
					.action((ctx, a) -> 
						new JSONObject()
					)
			)
		);

		def(array,
			choice(
				seq(arrayOpen, value, zeroMore(valueSep, value), arrayClose)
					.action((ctx, a) -> {
						JSONArray array = new JSONArray();
						for(Tuple2<Void, JSON> t : a.get2()) {
							array.add(t.get1());
						}
						return array;
					}),
				seq(arrayOpen, arrayClose)
					.action((ctx, a) -> 
						new JSONArray()
					)
			)
		);

		def(keyValue,
			seq(string, keyValueSep, value, ws)
				.action((ctx, a) -> 
					new Tuple2<>(a.get0(), a.get2())
				)
		);

		def(value,
			seq(choice(
				string,
				number,
				object,
				array,
				str("true").action((ctx, a) -> new JSONBool(true)),
				str("false").action((ctx, a) -> new JSONBool(false)),
				str("null").action((ctx, a) -> new JSONNull())
			), ws).action((ctx, a) -> 
				a.get0()
			)
		);

		Rule<Void> escape = ruleVoid("escape",
			seqN(str("\\"), ch('"', '\\', '/', 'b', 'f', 'n', 'r', 't'))
		);

		def(string,
			$(str("\""), 
			choice(
				escape, 
				seqN(not(ch('"', '\\')), ANY)
			).zeroMore(), 
			str("\"")).action((ctx, a) -> new JSONString(ctx.createTokenText(a)))
		);
	
		Rule<Void> integer = ruleVoid("integer",
			choice(
				str("0"),
				seqN(r('1', '9'), r('0', '9').zeroMore())
			)
		);

		Rule<Tuple3<Void, Optional<Void>, Void>> exp = rule("expr",
			seq(ch('E', 'e'), ch('+', '-').opt(), integer)
		);

		def(number, 
			choice(
				$(str("-").opt(), integer, str("."), r('0', '9').oneMore(), exp.opt())
					.action((ctx, a) -> 
						new JSONNumber(Double.parseDouble(ctx.createTokenText(a))
					)
				),
				$(str("-").opt(), integer)
					.action((ctx, a) -> 
						new JSONNumber(Long.parseLong(ctx.createTokenText(a))
					)
				)
			)
		);
	}
}
