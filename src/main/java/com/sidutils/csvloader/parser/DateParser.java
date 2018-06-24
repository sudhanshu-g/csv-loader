package com.sidutils.csvloader.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser implements Parser<Date> {

	private SimpleDateFormat parser;
	
	public DateParser(String pattern) {
		parser = new SimpleDateFormat(pattern);
	}

	@Override
	public Date parse(String source) throws ParseException {
		return parser.parse(source);
	}

	
}
