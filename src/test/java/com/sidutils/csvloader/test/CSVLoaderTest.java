package com.sidutils.csvloader.test;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.sidutils.csvloader.CsvLoader;
import com.sidutils.csvloader.parser.DateParser;
import com.sidutils.csvloader.parser.Parser;

public class CSVLoaderTest {

	private String pattern = "dd/MM/yyyy HH:mm";

	@Test
	public void testLoadCSV() {
		CsvLoader loader = new CsvLoader.Builder().skipHeader(true).delimeter("[,;]").build();
		InputStream in = getClass().getResourceAsStream("/Employees.csv");
		Parser<Date> dateParser = new DateParser(pattern);
		try {
			List<Employee> employees = loader.loadCsv(in, Employee.class);
			employees.forEach(e -> {
				System.out.printf(
						"EmployeeId: %d, Name: %s %s, CTC: %.2f LPA, Permanent: %s, Grade: %c, Last Seen: %s, Experience: %d\n",
						e.getEmployeeId(), e.getFirstName(), e.getLastName(), e.getCTC(), e.isPermanent(), e.getGrade(),
						e.getLastSeen(dateParser), e.getExperience());
			});

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Failed to load csv " + e);
		}
	}

}
