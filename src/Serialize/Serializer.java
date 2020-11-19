package Serialize;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;

import org.json.*;


public class Serializer {
	
	private static JSONObject main;
	private static JSONArray objects;
	private static IdentityHashMap<Object, String> ihm;
	
	public static String serializeObject(Object source) {
		
		main = new JSONObject();
		objects = new JSONArray();
		
		ihm = new IdentityHashMap<Object, String>();
		
		objects.put(serializeClassHelper(source));	
		main.put("objects", objects);
		
		
		return main.toString(4);
	}
	
	private static JSONObject serializeClassHelper(Object obj) {
		JSONObject classObj = new JSONObject();
		ihm.put(obj, Integer.toString(ihm.size()));
		classObj.put("class", obj.getClass().getName());
		classObj.put("id", ihm.get(obj));
		if(obj.getClass().isArray()) {
			classObj.put("type", "array");
		} else {
			classObj.put("type", "object");
		}
		
		classObj.put("fields", serializeFieldHelper(obj));
		
		return classObj;
		
	}
	
	private static JSONArray serializeFieldHelper(Object obj) {
		JSONArray fields = new JSONArray();
		Field[] fieldList = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fieldList.length; i++) {
			JSONObject field = new JSONObject();
			field.put("name", fieldList[i].getName());
			field.put("declaringclass", fieldList[i].getDeclaringClass().getName());
			try {
				if (fieldList[i].getType().isPrimitive()) {
					field.put("value", fieldList[i].get(obj));
				} else if (fieldList[i].get(obj).getClass().isArray()){
					
				} else {
					if(ihm.get(fieldList[i].get(obj)) == null) {
						objects.put(serializeClassHelper(fieldList[i].get(obj)));
					}
					field.put("reference", ihm.get(fieldList[i].get(obj)));
				}
				
				fields.put(field);
			} catch (IllegalArgumentException e) {
					e.printStackTrace();
			} catch (IllegalAccessException e) {
					e.printStackTrace();
			}
		}
		
		return fields;
	}
}


