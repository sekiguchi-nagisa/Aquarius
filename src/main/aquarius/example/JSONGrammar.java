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
			zeroMore(ch(' ', '\t', '\r', '\n'))
		);

		Rule<Void> objectOpen = rule("objectOpen",
			seqN(str("{"), ws)
		);

		Rule<Void> objectClose = rule("objectClose",
			seqN(str("}"), ws)
		);

		Rule<Void> arrayOpen = rule("arrayOpen",
			seqN(str("["), ws)
		);

		Rule<Void> arrayClose = rule("arrayClose",
			seqN(str("]"), ws)
		);

		Rule<Void> keyValueSep = rule("keyValueSep",
			seqN(ws, str(":"), ws)
		);

		Rule<Void> valueSep = rule("valueSep",
			seqN(str(","), ws)
		);

		// json definition
		def(json, 
			seq(ws, choice(object, array))
				.action((ctx, a) -> 
					a.get2()
				)
		);

		def(object, 
			choice(
				seq(objectOpen, keyValue, zeroMore(seq(valueSep, keyValue)), objectClose)
					.action((ctx, a) -> {
						JSONObject object = new JSONObject();
						for(Tuple2<Void, Tuple2<JSONString, JSON>> t : a.get3()) {
							object.add(t.get2());
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
				seq(arrayOpen, value, zeroMore(seq(valueSep, value)), arrayClose)
					.action((ctx, a) -> {
						JSONArray array = new JSONArray();
						for(Tuple2<Void, JSON> t : a.get3()) {
							array.add(t.get2());
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
				.action((ctx, a) 
					-> new Tuple2<>(a.get1(), a.get3())
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
				a.get1()
			)
		);

		Rule<Void> escape = rule("escape",
			seqN(str("\\"), ch('"', '\\', '/', 'b', 'f', 'n', 'r', 't'))
		);

		def(string,
			$(str("\""), 
			zeroMore(choice(
				escape, 
				seqN(not(ch('"', '\\')), ANY)
			)), 
			str("\"")).action((ctx, a) -> new JSONString(a.getText(ctx.getInputStream())))
		);
	
		Rule<Void> integer = rule("integer",
			choice(
				str("0"),
				seqN(r('1', '9'), zeroMore(r('0', '9')))
			)
		);

		Rule<Tuple3<Void, Optional<Void>, Void>> exp = rule("expr",
			seq(ch('E', 'e'), opt(ch('+', '-')), integer)
		);

		def(number, 
			choice(
				$(opt(str("-")), integer, str("."), oneMore(r('0', '9')), opt(exp))
					.action((ctx, a) -> 
						new JSONNumber(Double.parseDouble(a.getText(ctx.getInputStream()))
					)
				),
				$(opt(str("-")), integer)
					.action((ctx, a) -> 
						new JSONNumber(Long.parseLong(a.getText(ctx.getInputStream()))
					)
				)
			)
		);
	}
}
