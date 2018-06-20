package com.sidutils.csvloader;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.sidutils.csvloader.annotation.FieldIndex;

public class CsvLoaderProxy implements InvocationHandler {

	private String[] row;

	CsvLoaderProxy(String[] row){
		this.row = row;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		FieldIndex field = method.getAnnotation(FieldIndex.class);
		return row[field.value()];
	}

}
