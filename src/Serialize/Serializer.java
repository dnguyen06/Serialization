package Serialize;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
			ArrayList<Field> fields = new ArrayList<>();
			getFields(obj.getClass(), fields);
			classObj.put("fields", serializeFieldHelper(fields, obj));
		}
		
		
		
		return classObj;
		
	}
	
	private static JSONArray serializeFieldHelper(ArrayList<Field> fields, Object obj) {
		JSONArray f = new JSONArray();
		for (int i = 0; i < fields.size(); i++) {
			Field field= fields.get(i);
			field.setAccessible(true);
			JSONObject fieldJSON = new JSONObject();
			
			try {
				Field changeMap = fieldJSON.getClass().getDeclaredField("map");		// Referenced Code : https://towardsdatascience.com/create-an-ordered-jsonobject-in-java-fb9629247d76
				changeMap.setAccessible(true);
				changeMap.set(fieldJSON, new LinkedHashMap<>());
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
			
			fieldJSON.put("name", field.getName());
			fieldJSON.put("declaringclass", field.getDeclaringClass().getName());
			try {
				if (field.getType().isPrimitive()) {
					fieldJSON.put("value", field.get(obj));
				} else {
					if(ihm.get(field.get(obj)) == null) {
						objects.put(serializeClassHelper(field.get(obj)));
					}
					fieldJSON.put("reference", ihm.get(field.get(obj)));
				}
				
				f.put(fieldJSON);
			} catch (IllegalArgumentException e) {
					e.printStackTrace();
			} catch (IllegalAccessException e) {
					e.printStackTrace();
			}
		}
		
		return f;
	}
	
	// Recurse the superclasses to get all non-static fields
	private static void getFields(Class c, ArrayList<Field> fields) {
		if (c == null) {
			return;
		}
		
		for(Field f : c.getDeclaredFields()) {
			if (!Modifier.isStatic(f.getModifiers())) {
				fields.add(f);
			}
		}
		
		getFields(c.getSuperclass(), fields);
	}
	
}


