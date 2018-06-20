package com.sidutils.csvloader.parser;

import java.text.ParseException;

public interface Parser<T> {

	public T parse(String s) throws ParseException;
}
