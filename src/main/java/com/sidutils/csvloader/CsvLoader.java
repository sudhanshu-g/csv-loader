package com.sidutils.csvloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * Tool to load data from csv file into List/Stream of dynamically generated objects.
 * Follow the below steps to use it.
 * <ol>
 * 	<li>Create an interfae with getter methods mapped to fields in the csv file with @FieldIndex(&lt;field's column number in the file 0 based&gt;)</li>
 * 	<li>Initialize an object of CsvLoader using its Builder</li>
 * 	<li>Call one of the overridden methods to get the List/Stream of your interface type</li>
 * </ol>
 * @author sudhanshu-g
 *
 */
@Getter
public class CsvLoader {

	private String delimiter = ",";

	private boolean skipHeader = true;

	private CsvLoader() {

	}

	/**
	 * Builder class to create CsvLoader instance
	 * @author sudhanshu-g
	 *
	 */
	public static class Builder {
		private CsvLoader instance;

		public Builder() {
			instance = new CsvLoader();
		}

		/**
		 * Delimeter of the csv file fields. Default is comma(,). Use this method to change it.
		 * @param regex delimeter that splits the fields in the csv file. Supports regex 
		 * @return The Builder instance for method chaining
		 */
		public Builder delimeter(String regex) {
			instance.delimiter = regex;
			return this;
		}
		
		/**
		 * Skip reading the file header (the first line) in the csv file.
		 * Default: True
		 * @param skip turn it off if your csv file does not have a header line
		 * @return The Builder instance for method chaining
		 */
		public Builder skipHeader(boolean skip) {
			instance.skipHeader = skip;
			return this;
		}
		
		/**
		 * 
		 * @return the CsvLoader instance.
		 */
		public CsvLoader build() {
			return instance;
		}
	}

	/**
	 * Load the csv data as List of Objects 
	 * @param fileName Absolute file path of the csv file
	 * @param type The interface class
	 * @see com.sidutils.csvloader.CsvLoader
	 * @return List of dynamically generated objects from the interface supplied
	 * @throws FileNotFoundException if the supplied file is not available/accessible
	 */
	public <T> List<T> loadCsv(String fileName, Class<T> type) throws FileNotFoundException {
		File file = new File(fileName);
		return loadCsv(new FileInputStream(file),type);
	}
	
	/**
	 * Load the csv data as List of Objects 
	 * @param in InputStream for the csv file
	 * @param type The interface class
	 * @see com.sidutils.csvloader.CsvLoader
	 * @return List of dynamically generated objects from the interface supplied
	 * @throws FileNotFoundException if the supplied file is not available/accessible
	 */
	public <T> List<T> loadCsv(InputStream in, Class<T> type) throws FileNotFoundException {
		
		List<T> list = new ArrayList<>();
		try (Scanner sc = new Scanner(in)) {
			if(skipHeader && sc.hasNextLine()) {
				sc.nextLine();
			}
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] row = line.split(delimiter);
				T result = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { type },
						new CsvLoaderProxy(row));
				list.add(result);
			}
		}
		return list;
	}
	
	/**
	 * Load the csv data as Stream of Objects 
	 * @param in InputStream for the csv file
	 * @param type The interface class
	 * @see com.sidutils.csvloader.CsvLoader
	 * @return Stream of dynamically generated objects from the interface supplied
	 * @throws FileNotFoundException if the supplied file is not available/accessible
	 */
	public <T> Stream<T> loadCsvStream(InputStream in, Class<T> type) throws IOException {
		Stream<String> lines = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8"))).lines();
		return loadStream(lines, type);
	}

	/**
	 * Load the csv data as Stream of Objects 
	 * @param fileName Absolute file path of the csv file
	 * @param type The interface class
	 * @see com.sidutils.csvloader.CsvLoader
	 * @return Stream of dynamically generated objects from the interface supplied
	 * @throws FileNotFoundException if the supplied file is not available/accessible
	 */
	public <T> Stream<T> loadCsvStream(String fileName, Class<T> type) throws IOException {
		Stream<String> lines = Files.lines(Paths.get(fileName));
		return loadStream(lines, type);
	}
	
	
	private <T> Stream<T> loadStream(Stream<String> lines, Class<T> type) {
		int skipLines = (skipHeader)?1:0;
		return lines.skip(skipLines).map(line -> {
			String[] row = line.split(delimiter);
			return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { type },
					new CsvLoaderProxy(row));
		});
	}
}
