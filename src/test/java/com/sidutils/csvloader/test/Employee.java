package com.sidutils.csvloader.test;

import java.util.Date;

import com.sidutils.csvloader.annotation.FieldIndex;
import com.sidutils.csvloader.parser.Parser;

public interface Employee {
	
	@FieldIndex(0)
	public long getEmployeeId();
	
	@FieldIndex(1)
	public String getFirstName();
	
	@FieldIndex(2)
	public String getLastName();
	
	@FieldIndex(3)
	public double getCTC();
	
	@FieldIndex(4)
	public boolean isPermanent();
	
	@FieldIndex(5)
	public char getGrade();
	
	@FieldIndex(6)
	public Date getLastSeen(Parser<Date> parser);
	
	@FieldIndex(7)
	public int getExperience();
	
}
