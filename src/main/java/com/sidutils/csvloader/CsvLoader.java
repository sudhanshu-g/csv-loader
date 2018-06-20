package com.sidutils.csvloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class CsvLoader<T> {

	private String delimiter = ",";

	private boolean skipHeader = true;

	private CsvLoader() {

	}

	public static class Builder {
		private CsvLoader instance;

		public Builder() {
			instance = new CsvLoader<>();
		}

		public Builder setDelimeter(String regex) {
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

	public List<T> loadCsv(String fileName, Class<T> type) throws FileNotFoundException {
		File file = new File(fileName);
		List<T> list = new ArrayList<>();
		try (Scanner sc = new Scanner(new FileInputStream(file))) {
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

	public Stream<T> loadCsvStream(String fileName, Class<T> type) throws IOException {
		Stream<String> in = Files.lines(Paths.get(fileName));
		return in.skip(0).map(line -> {
			String[] row = line.split(delimiter);
			return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { type },
					new CsvLoaderProxy(row));
		});
	}
}
