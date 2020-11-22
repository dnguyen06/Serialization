package Objects;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.IdentityHashMap;

public class Inspector {
	
	private static IdentityHashMap<Object, Integer> ihm;

	public static void inspect(Object obj, boolean recursive) {
		Class c = obj.getClass();
		ihm = new IdentityHashMap<Object, Integer>();
		inspectClass(c, obj, recursive, 0);
		
	}

	private static void inspectClass(Class c, Object obj, boolean recursive, int depth) {
		
		
		ihm.put(obj, ihm.size());
		
		String className = c.getName();	
		
		String tabs = getTabs(depth);
		System.out.println(tabs + "CLASS");
		System.out.println(tabs + "Class: " + className);
		
		if (c.getSuperclass() == null) {
			System.out.println(tabs + "Superclass: NONE");
		} else {
			System.out.println(tabs + "SUPERCLASS -> Recursively Inspect");
			System.out.println(tabs + "Superclass : " + c.getSuperclass().getName());
			inspectClass(c.getSuperclass(), obj, recursive, ++depth);
			depth--;
		}

			

		System.out.println(tabs + "FIELDS( " + className + " )");
		Field[] fields = c.getDeclaredFields();
		if (fields.length == 0) {
			System.out.println(tabs + "Fields-> NONE");
		} else {
			System.out.println(tabs + "Fields->");
			for (int i = 0; i < fields.length; i++) {
				if (Modifier.isStatic(fields[i].getModifiers())) {
					continue;
				}
				System.out.println(tabs + " FIELD");
				System.out.println(tabs + "  " + "Name: " + fields[i].getName());
				System.out.println(tabs + "  " + "Type: " + fields[i].getType());
				int modifier = fields[i].getModifiers();
				System.out.println(tabs + "  " + "Modifiers: " + Modifier.toString(modifier));
				fields[i].setAccessible(true);
				try {
					
					Object object = fields[i].get(obj);
					if (fields[i].getType().isPrimitive()) {
						System.out.println(tabs + "  " + "Value: " + object);
					} else if (object == null) {
						System.out.println(tabs + "  " + "Value (ref): null");
					} else if (object.getClass().isArray()) {
						System.out.println(tabs + "  " + "Component Type: "
								+ object.getClass().getComponentType());
						System.out.println(tabs + "  " + "Length: " + Array.getLength(object));
						System.out.println(tabs + "  " + "Entries->");
						for (int j = 0; j < Array.getLength(object); j++) {
							if (object.getClass().getComponentType().equals(int.class)) {
								System.out.println(tabs + "   " + "Value: " + Array.get(object, j));
							} else {
								if (Array.get(object, j) == null) {
									System.out.println(tabs + "   " + "Reference: null");
								} else {
									System.out.println(
											tabs + "  " + " Reference: " + Array.get(object, j));
									System.out.println(tabs + "  -> Recursively Inspect");
									inspectClass(Array.get(object, j).getClass(), Array.get(object, j), recursive, ++depth);
									depth--;
								}
								
							}
							
						}

					} else if (!recursive) {

						System.out.println(tabs + "  " + "Value (ref): " + object.getClass().getName()
								+ "@" + object.hashCode());

					} else {
						if(ihm.get(object) != null) {
							System.out.println(tabs + "  " + "Value (ref): " + object.getClass().getName()
									+ "@" + object.hashCode());
							System.out.println(tabs + "   -> Already Inspected");
							return;
						} else {
							System.out.println(tabs + "  " + "Value (ref): " + object.getClass().getName()
									+ "@" + object.hashCode());
							System.out.println(tabs + "   -> Recursively inspect");
							inspectClass(object.getClass(), object, recursive, ++depth);
							depth--;
						}
							

						

					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				}
			}

		}

	//}
	

	private static String getTabs(int depth) {
		String tabs = "";
		for (int i = 0; i < depth; i++) {
			tabs += "\t";
		}
		return tabs;
	}


}
