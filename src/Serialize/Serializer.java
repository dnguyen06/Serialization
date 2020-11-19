package Serialize;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;

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
		
		try {
			Field changeMap = classObj.getClass().getDeclaredField("map");		// Referenced Code : https://towardsdatascience.com/create-an-ordered-jsonobject-in-java-fb9629247d76
			changeMap.setAccessible(true);
			changeMap.set(classObj, new LinkedHashMap<>());
			changeMap.setAccessible(false);
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
		  catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
		
		ihm.put(obj, Integer.toString(ihm.size()));
		classObj.put("class", obj.getClass().getName());
		classObj.put("id", ihm.get(obj));
		if(obj.getClass().isArray()) {
			classObj.put("type", "array");
			classObj.put("length", Array.getLength(obj));
			JSONArray entries = new JSONArray();
			for (int i = 0; i < Array.getLength(obj); i++) {
				JSONObject entry = new JSONObject();
				if (obj.getClass().getComponentType().isPrimitive()) {
					if (Array.get(obj, i) == null) {
						entry.put("value", "null");
					} else {
						entry.put("value", Array.get(obj, i));
					}
				} else {
					if (Array.get(obj, i) == null) {
						entry.put("reference", "null");
					} else {
						if(ihm.get(Array.get(obj, i)) == null) {
							objects.put(serializeClassHelper(Array.get(obj, i)));
						}
						entry.put("reference", ihm.get(Array.get(obj, i)));
					}
				}
				
				entries.put(entry);
			}
			
			classObj.put("entries", entries);
			
			
		} else {
			classObj.put("type", "object");
			classObj.put("fields", serializeFieldHelper(obj));
		}
		
		
		
		return classObj;
		
	}
	
	private static JSONArray serializeFieldHelper(Object obj) {
		JSONArray fields = new JSONArray();
		Field[] fieldList = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fieldList.length; i++) {
			fieldList[i].setAccessible(true);
			JSONObject field = new JSONObject();
			
			try {
				Field changeMap = field.getClass().getDeclaredField("map");		// Referenced Code : https://towardsdatascience.com/create-an-ordered-jsonobject-in-java-fb9629247d76
				changeMap.setAccessible(true);
				changeMap.set(field, new LinkedHashMap<>());
				changeMap.setAccessible(false);
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			  catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			field.put("name", fieldList[i].getName());
			field.put("declaringclass", fieldList[i].getDeclaringClass().getName());
			try {
				if (fieldList[i].getType().isPrimitive()) {
					field.put("value", fieldList[i].get(obj));
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


