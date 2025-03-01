package com.icst.android.appstudio.beans.utils;

import com.icst.android.appstudio.beans.DatatypeBean;

public final class BuiltInDatatypes {

	// New Class<T> type definition
	public static DatatypeBean getClassDatatype() {
		// Superclass
		DatatypeBean object = new DatatypeBean("java.lang.Object", "Object");

		// Interfaces
		DatatypeBean serializable = new DatatypeBean("java.io.Serializable", "Serializable");
		DatatypeBean genericDeclaration = new DatatypeBean(
				"java.lang.reflect.GenericDeclaration",
				"GenericDeclaration");
		DatatypeBean type = new DatatypeBean("java.lang.reflect.Type", "Type");
		DatatypeBean annotatedElement = new DatatypeBean(
				"java.lang.reflect.AnnotatedElement",
				"AnnotatedElement");

		// Class
		DatatypeBean classType = new DatatypeBean("java.lang.Class", "Class");
		classType.addSuperType(object);
		classType.addSuperType(serializable);
		classType.addSuperType(genericDeclaration);
		classType.addSuperType(type);
		classType.addSuperType(annotatedElement);

		return classType;
	}

	public static DatatypeBean getPrimitiveBooleanDatatype() {
		return new DatatypeBean("boolean", "boolean");
	}

	public static DatatypeBean getPrimitiveIntegerDatatype() {
		return new DatatypeBean("int", "int");
	}

	public static DatatypeBean getIntegerDatatype() {
		DatatypeBean object = new DatatypeBean("java.lang.Object", "Object");
		DatatypeBean serializable = new DatatypeBean("java.io.Serializable", "Serializable");
		DatatypeBean comparable = new DatatypeBean("java.lang.Comparable", "Comparable");
		DatatypeBean number = new DatatypeBean("java.lang.Number", "Number");

		// Create Integer type
		DatatypeBean integer = new DatatypeBean("java.lang.Integer", "Integer");

		// Set up hierarchy: String -> Number -> Object
		integer.addSuperType(number);
		number.addSuperType(object);

		// Add interfaces implemented by Integer
		integer.addSuperType(serializable);
		integer.addSuperType(comparable);
		integer.addSuperType(number);
		integer.addSuperType(getPrimitiveIntegerDatatype());

		return integer;
	}

	public static DatatypeBean getStringDatatype() {
		// Create core types
		DatatypeBean object = new DatatypeBean("java.lang.Object", "Object");
		DatatypeBean serializable = new DatatypeBean("java.io.Serializable", "Serializable");
		DatatypeBean comparable = new DatatypeBean("java.lang.Comparable", "Comparable");
		DatatypeBean charSequence = new DatatypeBean("java.lang.CharSequence", "CharSequence");

		// Create String type
		DatatypeBean string = new DatatypeBean("java.lang.String", "String");

		// Set up hierarchy: String -> Object
		string.addSuperType(object);

		// Add interfaces implemented by String
		string.addSuperType(serializable);
		string.addSuperType(comparable);
		string.addSuperType(charSequence);

		return string;
	}
}
