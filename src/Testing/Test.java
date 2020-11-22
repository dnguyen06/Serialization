package Testing;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;


import Objects.ObjectA;
import Objects.ObjectC;
import Objects.ObjectD;
import Serialize.Serializer;

class Test {

	
	@org.junit.jupiter.api.Test
	public void testObjectA() {
		ObjectA a = new ObjectA();
		a.x = 1;
		a.y = (float) 2.5;
		
		String jsonString = Serializer.serializeObject(a);
		JSONObject json = new JSONObject(jsonString);
		JSONArray objects = json.getJSONArray("objects");
		JSONObject classObj = (JSONObject) objects.get(0);
		JSONArray fields = classObj.getJSONArray("fields");
		JSONObject fieldX = (JSONObject) fields.get(0);
		JSONObject fieldY = (JSONObject) fields.get(1);
		
		String expectedX = "1";
		String expectedY = "2.5";
		String actualX = fieldX.get("value").toString();
		String actualY = fieldY.get("value").toString();
		
		assertEquals(expectedX, actualX);
		assertEquals(expectedY, actualY);
	}
	
	@org.junit.jupiter.api.Test
	public void testObjectC() {
		ObjectC c = new ObjectC();
		c.array[0] = 40;
		
		String jsonString = Serializer.serializeObject(c);
		JSONObject json = new JSONObject(jsonString);
		JSONArray objects = json.getJSONArray("objects");
		JSONObject classObj = (JSONObject) objects.get(0);
		JSONArray entries = classObj.getJSONArray("entries");
		JSONObject entry = (JSONObject) entries.get(0);
		
		String expected = "40";
		String actual = entry.get("value").toString();

		
		assertEquals(expected, actual);

	}
	
	@org.junit.jupiter.api.Test
	public void testObjectD() {
		ObjectD d = new ObjectD();
		ObjectA a = new ObjectA();
		a.x = 10;
		a.y = (float) 14.6;
		d.arrayA[0] = a;
		
		String jsonString = Serializer.serializeObject(d);
		JSONObject json = new JSONObject(jsonString);
		JSONArray objects = json.getJSONArray("objects");
		JSONObject classObj = (JSONObject) objects.get(0);
		JSONArray fields = classObj.getJSONArray("fields");
		JSONObject fieldX = (JSONObject) fields.get(0);
		JSONObject fieldY = (JSONObject) fields.get(1);
		
		String expectedX = "10";
		String expectedY = "14.6";
		String actualX = fieldX.get("value").toString();
		String actualY = fieldY.get("value").toString();
		
		assertEquals(expectedX, actualX);
		assertEquals(expectedY, actualY);;

	}

}
