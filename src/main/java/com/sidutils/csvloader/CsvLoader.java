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

@Getter
public class CsvLoader {

	private String delimiter = ",";

	private boolean skipHeader = true;

	private CsvLoader() {

	}

	public static class Builder {
		private CsvLoader instance;

		public Builder() {
			instance = new CsvLoader();
		}

		public Builder delimeter(String regex) {
			instance.delimiter = regex;
			return this;
		}
		
		public Builder skipHeader(boolean skip) {
			instance.skipHeader = skip;
			return this;
		}
		
		public CsvLoader build() {
			return instance;
		}
	}

	public <T> List<T> loadCsv(String fileName, Class<T> type) throws FileNotFoundException {
		File file = new File(fileName);
		return loadCsv(new FileInputStream(file),type);
	}
	
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
	
	public <T> Stream<T> loadCsvStream(InputStream in, Class<T> type) throws IOException {
		Stream<String> lines = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8"))).lines();
		return loadStream(lines, type);
	}

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
