package Objects;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Inspector {
	

	public void inspect(Object obj, boolean recursive) {
		Class c = obj.getClass();
		inspectClass(c, obj, recursive, 0);
	}

	private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

		String className = c.getName();
		
		
		
		String tabs = getTabs(depth);
		System.out.println(tabs + "CLASS");
		System.out.println(tabs + "Class: " + className);
		if (obj.getClass().isArray()) {
			System.out.println(tabs + " " + "Component Type: " + obj.getClass().getComponentType());
			System.out.println(tabs + " " + "Length: " + Array.getLength(obj));
			System.out.println(tabs + " " + "Entries->");
			for (int i = 0; i < Array.getLength(obj); i++) {
				if (Array.get(obj, i) != null) {
					System.out.println(
							tabs + "  " + "Value (ref): " + Array.get(obj, i) + "@" + Array.get(obj, i).hashCode());
					if (recursive) {
						System.out.println(tabs + "  -> Recursively Inspect");
						inspectClass(obj.getClass().getComponentType(), Array.get(obj, i), recursive, ++depth);
						depth--;
					}

				} else {
					System.out.println(tabs + "  " + "Value: " + Array.get(obj, i));
				}

			}

		} else {

			// DEBUG: System.out.println(tabs + "Superclass: " + c.getSuperclass() + "
			// Current Class: " + c.getName());
			if (c.getSuperclass() == null) {
				System.out.println(tabs + "Superclass: NONE");
			} else {
				System.out.println(tabs + "SUPERCLASS -> Recursively Inspect");
				System.out.println(tabs + "Superclass : " + c.getSuperclass().getName());
				inspectClass(c.getSuperclass(), obj, recursive, ++depth);
				depth--;
			}
			System.out.println(tabs + "INTERFACES( " + className + " )");
			Class[] interfaces = c.getInterfaces();
			if (interfaces.length == 0) {
				System.out.println(tabs + "Interfaces-> NONE");
			} else {
				System.out.println("HELLO: " + interfaces[0].getName().equals("java.io.Serializable"));
				System.out.println(tabs + "Interfaces->");
				for (int i = 0; i < interfaces.length; i++) {
					System.out.println(tabs + " INTERFACE -> Recursively Inspect");
					System.out.println(tabs + " " + interfaces[i].getName());
					inspectClass(interfaces[i], obj, recursive, ++depth);
					depth--;
				}
			}

			System.out.println(tabs + "CONSTRUCTORS( " + className + " )");
			Constructor[] constructors = c.getDeclaredConstructors();
			if (constructors.length == 0) {
				System.out.println(tabs + "Constructor-> NONE");
			} else {
				System.out.println(tabs + "Constructors->");
				for (int i = 0; i < constructors.length; i++) {
					System.out.println(tabs + " CONSTRUCTOR");
					System.out.println(tabs + "  " + "Name: " + constructors[i].getName());
					Class[] parameters = constructors[i].getParameterTypes();
					System.out.println(tabs + "  " + "Parameter types:");
					if (parameters.length != 0) {
						for (int j = 0; j < parameters.length; j++) {
							System.out.println(tabs + "  " + parameters[j]);
						}
					}
					int modifier = constructors[i].getModifiers();
					if (modifier == 0) {
						System.out.println(tabs + "  " + "Modifiers: NONE");
					} else {
						System.out.println(tabs + "  " + "Modifiers: " + Modifier.toString(modifier));
					}
				}

			}

			System.out.println(tabs + "METHODS( " + className + " )");
			Method[] methods = c.getDeclaredMethods();
			if (methods.length == 0) {
				System.out.println(tabs + "Methods-> NONE");
			} else {
				System.out.println(tabs + "Methods->");
				for (int i = 0; i < methods.length; i++) {
					System.out.println(tabs + " METHOD");
					System.out.println(tabs + "  " + "Name: " + methods[i].getName());
					Class[] exceptions = methods[i].getExceptionTypes();
					if (exceptions.length == 0) {
						System.out.println(tabs + "  " + "Exceptions-> NONE");
					} else {
						System.out.println(tabs + "  " + "Exceptions->");
						for (int j = 0; j < exceptions.length; j++) {
							System.out.println(tabs + "  " + exceptions[j]);
						}

					}
					Class[] parameters = methods[i].getParameterTypes();
					if (parameters.length == 0) {
						System.out.println(tabs + "  " + "Parameter types-> NONE");
					} else {
						System.out.println(tabs + "  " + "Parameter types->");
						for (int j = 0; j < parameters.length; j++) {
							System.out.println(tabs + "  " + parameters[j]);
						}
					}
					System.out.println(tabs + "  " + "Return type: " + methods[i].getReturnType());
					int modifier = methods[i].getModifiers();
					System.out.println(tabs + "  " + "Modifiers: " + Modifier.toString(modifier));
				}
			}

			System.out.println(tabs + "FIELDS( " + className + " )");
			Field[] fields = c.getDeclaredFields();
			if (fields.length == 0) {
				System.out.println(tabs + "Fields-> NONE");
			} else {
				System.out.println(tabs + "Fields->");
				for (int i = 0; i < fields.length; i++) {
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
								System.out.println(tabs + "   " + "Value: " + Array.get(object, j));
							}

						} else if (!recursive) {

							System.out.println(tabs + "  " + "Value (ref): " + object.getClass().getName()
									+ "@" + object.hashCode());

						} else {
							System.out.println(tabs + "  " + "Value (ref): " + object.getClass().getName()
									+ "@" + object.hashCode());
							System.out.println(tabs + "   -> Recursively inspect");
							inspectClass(object.getClass(), object, recursive, ++depth);
							depth--;

						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

				}
			}

		}

	}

	private String getTabs(int depth) {
		String tabs = "";
		for (int i = 0; i < depth; i++) {
			tabs += "\t";
		}
		return tabs;
	}


}
