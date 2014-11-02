package aquarius.example;

import static aquarius.matcher.Expressions.*;
import static aquarius.example.JSON.*;

import java.util.List;

import aquarius.matcher.Grammar;
import aquarius.misc.Tuple2;
import aquarius.runtime.Token;

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
		Rule<Void> ws = rule("ws", 
			zeroMore(ch(' ', '\t', '\r', '\n')).action((ctx, a) -> null)
		);

		Rule<Void> objectOpen = rule("objectOpen",
			seq2(str("{"), ws).action((ctx, a) -> a.get2())
		);

		Rule<Void> objectClose = rule("objectClose",
			seq2(str("}"), ws).action((ctx, a) -> a.get2())
		);

		Rule<Void> arrayOpen = rule("arrayOpen",
			seq2(str("["), ws).action((ctx, a) -> a.get2())
		);

		Rule<Void> arrayClose = rule("arrayClose",
			seq2(str("]"), ws).action((ctx, a) -> a.get2())
		);

		Rule<Void> keyValueSep = rule("keyValueSep",
			seq3(ws, str(":"), ws).action((ctx, a) -> a.get1())
		);

		Rule<Void> valueSep = rule("valueSep",
			seq2(str(","), ws).action((ctx, a) -> a.get2())
		);

		// json definition
		def(json, 
			seq2(ws, choice(object, array)).action((ctx, a) -> a.get2())
		);

		def(object, 
			choice(
				seq4(objectOpen, keyValue, zeroMore(seq2(valueSep, keyValue)), objectClose).
					action((ctx, a) -> {
						JSONObject object = new JSONObject();
						object.put(a.get2().get1(), a.get2().get2());
						for(Tuple2<Void, Tuple2<JSONString, JSON>> tuple2 : a.get3()) {
							object.add(tuple2.get2());
						}
						return object;
					}),
				seq2(objectOpen, objectClose).action((ctx, a) -> new JSONObject())
			)
		);

		def(array,
			choice(
				seq4(arrayOpen, value, zeroMore(seq2(valueSep, value)), arrayClose).
					action((ctx, a) -> {
						JSONArray array = new JSONArray();
						array.add(a.get2());
						for(Tuple2<Void, JSON> tuple2 : a.get3()) {
							array.add(tuple2.get2());
						}
						return array;
					}),
				seq2(arrayOpen, arrayClose).action((ctx, a) -> new JSONArray())
			)
		);

		def(keyValue,
			seq4(string, keyValueSep, value, ws).
				action((ctx, a) -> new Tuple2<>(a.get1(), a.get3()))
		);

		def(value,
			seq2(choice(
				string,
				number,
				object,
				array,
				str("true").action((ctx, a) -> new JSONBool(true)),
				str("false").action((ctx, a) -> new JSONBool(false)),
				str("null").action((ctx, a) -> NULL)
			), ws).action((ctx, a) -> a.get1())
		);

		Rule<List<Token>> escape = rule("escape",
			seq(str("\\"), ch('"', '\\', '/', 'b', 'f', 'n', 'r', 't'))
		);

		def(string,
			$(str("\""), 
			zeroMore(choice($(escape), $(not(ch('"', '\\')), ANY))), 
			str("\"")).action((ctx, a) -> 
				new JSONString(a.getText(ctx.getInputStream())))
		);
	
		Rule<Token> integer = rule("integer",
			choice(
				str("0"),
				$(r('1', '9'), zeroMore(r('0', '9'))))
		);

		Rule<Token> exp = rule("expr",
			$(ch('E', 'e'), opt(ch('+', '-')), integer)
		);

		def(number, 
			choice(
				$(opt(str("-")), integer, str("."), oneMore(r('0', '9')), opt(exp)).
					action((ctx, a) -> 
						new JSONNumber(Double.parseDouble(a.getText(ctx.getInputStream())))),
				$(opt(str("-")), integer).
					action((ctx, a) -> 
						new JSONNumber(Long.parseLong(a.getText(ctx.getInputStream()))))
			)
		);
	}
}
