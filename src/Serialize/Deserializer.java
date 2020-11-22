package Serialize;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;

import org.json.*;

public class Deserializer {
	
	private static IdentityHashMap<Integer, Object> ihm;
	private static Object[] holders;
	
	public static Object deserializeObject (String source) throws ClassNotFoundException, JSONException, InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		
		ihm = new IdentityHashMap<Integer, Object>();
		JSONObject objects = new JSONObject(source);
		JSONArray classes = objects.getJSONArray("objects");
		
		for (int i = 0; i < classes.length(); i ++) {
			JSONObject classObj = (JSONObject) classes.get(i);
			if (classObj.get("type").toString().equals("array")) {
				int id = Integer.parseInt(classObj.get("id").toString());
				int length = Integer.parseInt(classObj.get("length").toString());
				Class c = Class.forName(classObj.get("class").toString());
				Class type = c.getComponentType();
				Object obj = Array.newInstance(type, length);
				ihm.put(id, obj);

			} else {
				Class c = Class.forName(classObj.get("class").toString());
				int id = Integer.parseInt(classObj.get("id").toString());
				Object obj = c.newInstance();
				ihm.put(id, obj);

			}
			
		}
		
		
		for (int i = 0; i < classes.length(); i++) {
			JSONObject classObj = (JSONObject) classes.get(i);
			if (classObj.get("type").toString().equals("array")) {
				JSONArray entries = classObj.getJSONArray("entries");
				int id = Integer.parseInt(classObj.get("id").toString());
				for (int j = 0; j < entries.length(); j++) {
					JSONObject entryObj = (JSONObject) entries.get(j);
					setEntry(ihm.get(id), entryObj, j);
		
				}
			} else {
				JSONArray fields = classObj.getJSONArray("fields");
				int id = Integer.parseInt(classObj.get("id").toString());
				for (int j = 0; j < fields.length(); j++) {
					JSONObject fieldObj = (JSONObject) fields.get(j);
					Class c = Class.forName(fieldObj.get("declaringclass").toString());
					setField(ihm.get(id), fieldObj, c);
			
				}
			}
			
		}
					
		return ihm.get(0);

	}
	
	public static void setEntry(Object obj, JSONObject entryObj, int index) {
		Class type = obj.getClass().getComponentType();
		if (type.equals(int.class)) {
			Array.set(obj, index, Integer.parseInt(entryObj.get("value").toString()));
		} else {
			if (!entryObj.get("reference").toString().equals("null")) {
				int reference = Integer.parseInt(entryObj.get("reference").toString());
				Array.set(obj, index, ihm.get(reference));
			}
			
		}
	}
	
	
	public static void setField(Object obj, JSONObject fieldObj, Class c) throws IllegalArgumentException, IllegalAccessException, JSONException, NoSuchFieldException, SecurityException {
		Field field = c.getDeclaredField(fieldObj.get("name").toString());
		field.setAccessible(true);
		
		if (field.getType().equals(int.class)) {
			field.set(obj, Integer.parseInt(fieldObj.get("value").toString()));
		} else if (field.getType().equals(float.class)) {
			field.set(obj, Float.parseFloat(fieldObj.get("value").toString()));
		} else if (field.getType().equals(boolean.class)){
			field.set(obj, Boolean.parseBoolean(fieldObj.get("value").toString()));
		} else {
			int reference = Integer.parseInt(fieldObj.get("reference").toString());
			field.set(obj, ihm.get(reference));
		}
	}

}
