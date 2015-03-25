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

import static org.junit.Assert.*;

import org.junit.Test;

import aquarius.CommonStream;
import aquarius.ParsedResult;
import aquarius.ParserFactory;
import aquarius.example.JSON.JSONArray;
import aquarius.example.JSON.JSONObject;
import aquarius.example.JSON.JSONString;

public class JSONParserTest {

/**
{
  "glossary": {
    "title": "example glossary",
    "GlossDiv": {
      "title": "S",
      "GlossList": {
        "GlossEntry": {
          "ID": "SGML",
          "SortAs": "SGML",
          "GlossTerm": "Standard Generalized Markup Language",
          "Acronym": "SGML",
          "Abbrev": "ISO 8879:1986",
          "GlossDef": {
            "para": "A meta-markup language, used to create markup languages such as DocBook.",
            "GlossSeeAlso": ["GML", "XML"]
          },
          "GlossSee": "markup"
        }
      }
    }
  }
}
*/
	@Test
	public void test() {
		final String jsonString = 
				"{" + "\n" +
				"  \"glossary\": {" + "\n" +
				"    \"title\": \"example glossary\"," + "\n" +
				"    \"GlossDiv\": {" + "\n" +
				"      \"title\": \"S\"," + "\n" +
				"      \"GlossList\": {" + "\n" +
				"        \"GlossEntry\": {" + "\n" +
				"          \"ID\": \"SGML\"," + "\n" +
				"          \"SortAs\": \"SGML\"," + "\n" +
				"          \"GlossTerm\": \"Standard Generalized Markup Language\"," + "\n" +
				"          \"Acronym\": \"SGML\"," + "\n" +
				"          \"Abbrev\": \"ISO 8879:1986\"," + "\n" +
				"          \"GlossDef\": {" + "\n" +
				"            \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\"," + "\n" +
				"            \"GlossSeeAlso\": [\"GML\", \"XML\"]" + "\n" +
				"          }," + "\n" +
				"          \"GlossSee\": \"markup\"" + "\n" +
				"        }" + "\n" +
				"      }" + "\n" +
				"    }" + "\n" +
				"  }" + "\n" +
				"}";

		CommonStream input = new CommonStream("ex", jsonString);
		JSONParser parser = ParserFactory.createParser(JSONParser.class);
		ParsedResult<JSON> result = parser.json().parse(input);
		assertTrue(result.isSucess());
		assertEquals(input.getPosition(), input.getBufferSize());

		JSON json = result.getValue();
		assertTrue(json instanceof JSONObject);

		JSONObject jsonObject = (JSONObject) json;
		assertEquals(1, jsonObject.size());

		{
			JSONObject json_glossary = (JSONObject) jsonObject.get(new JSONString("glossary"));
			assertEquals(2, json_glossary.size());

			{
				JSONString json_title = (JSONString) json_glossary.get(new JSONString("title"));
				assertEquals("example glossary", json_title.getValue());

				JSONObject json_GlossDiv = (JSONObject) json_glossary.get(new JSONString("GlossDiv"));
				assertEquals(2, json_GlossDiv.size());

				{
					json_title = (JSONString) json_GlossDiv.get(new JSONString("title"));
					assertEquals("S", json_title.getValue());

					JSONObject json_GlossList = (JSONObject) json_GlossDiv.get(new JSONString("GlossList"));
					assertEquals(1, json_GlossList.size());

					{
						JSONObject json_GlossEntry = (JSONObject) json_GlossList.get(new JSONString("GlossEntry"));
						assertEquals(7, json_GlossEntry.size());

						{
							JSONString json_ID = (JSONString) json_GlossEntry.get(new JSONString("ID"));
							assertEquals("SGML", json_ID.getValue());

							JSONString json_SortAs = (JSONString) json_GlossEntry.get(new JSONString("SortAs"));
							assertEquals("SGML", json_SortAs.getValue());

							JSONString json_GlossTerm = (JSONString) json_GlossEntry.get(new JSONString("GlossTerm"));
							assertEquals("Standard Generalized Markup Language", json_GlossTerm.getValue());

							JSONString json_Acronym = (JSONString) json_GlossEntry.get(new JSONString("Acronym"));
							assertEquals("SGML", json_Acronym.getValue());

							JSONString json_Abbrev = (JSONString) json_GlossEntry.get(new JSONString("Abbrev"));
							assertEquals("ISO 8879:1986", json_Abbrev.getValue());

							JSONObject json_GlossDef = (JSONObject) json_GlossEntry.get(new JSONString("GlossDef"));
							assertEquals(2, json_GlossDef.size());

							{
								JSONString json_para = (JSONString) json_GlossDef.get(new JSONString("para"));
								assertEquals("A meta-markup language, used to create markup languages such as DocBook.", json_para.getValue());

								JSONArray json_GlossSeeAlso = (JSONArray) json_GlossDef.get(new JSONString("GlossSeeAlso"));
								assertEquals(2, json_GlossSeeAlso.size());

									JSONString json_GlossSeeAlso_0 = (JSONString) json_GlossSeeAlso.get(0);
									assertEquals("GML", json_GlossSeeAlso_0.getValue());

									JSONString json_GlossSeeAlso_1 = (JSONString) json_GlossSeeAlso.get(1);
									assertEquals("XML", json_GlossSeeAlso_1.getValue());
							}

							JSONString json_GlossSee = (JSONString) json_GlossEntry.get(new JSONString("GlossSee"));
							assertEquals("markup", json_GlossSee.getValue());
						}
					}
				}
			}
		}
	}

}
