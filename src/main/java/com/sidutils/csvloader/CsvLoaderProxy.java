package com.sidutils.csvloader;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.ParseException;

import com.sidutils.csvloader.annotation.FieldIndex;
import com.sidutils.csvloader.parser.Parser;

public class CsvLoaderProxy implements InvocationHandler {

	private String[] row;

	CsvLoaderProxy(String[] row) {
		this.row = row;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		FieldIndex field = method.getAnnotation(FieldIndex.class);
		String value = row[field.value()];

		Class<?> type = method.getReturnType();

		if (args != null && args.length == 1) {
			if (args[0] instanceof Parser) {
				Parser<?> parser = (Parser<?>) args[0];
				return parser.parse(value);
			}
			throw new IllegalArgumentException(String.format("Argument %s in method %s is not a Parser",args[0],method.getName()));
		}

		switch (type.getName()) {
		case "int":
		case "java.lang.Integer":
			return Integer.parseInt(value);
		case "long":
		case "java.lang.Long":
			return Long.parseLong(value);
		case "float":
		case "java.lang.Float":
			return Float.parseFloat(value);
		case "double":
		case "java.lang.Double":
			return Double.parseDouble(value);
		case "boolean":
		case "java.lang.Boolean":
			return Boolean.parseBoolean(value);
		case "char":
		case "java.lang.Character":
			if (value.length() == 1)
				return Character.valueOf(value.charAt(0));
			throw new ParseException(value + " can't be parsed to " + type.getName(), 0);
		}

		return value;
	}

}
