# csv-loader
A tool to load csv file data into List/Stream of Objects

## How to Use
	1. Create an interface with getter methods for the fields you want to extract from the csv file. No implementation of the interface required.
	
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
		
	2. Mark the methods with **@FieldIndex** with 0 based index of the field in the csv file. 
	
	3. Create an instance of CsvLoader class using its Builder.
	
	4. Use one of the overridden methods to load the data in your csv file into List or Stream of Objects of your interface type.
	
	5. Bingo!!! You are ready to operate on your csv data. 
		
	
