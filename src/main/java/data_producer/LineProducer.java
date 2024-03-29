package data_producer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LineProducer implements DataProducer<String> {
	private BufferedReader reader;
	private int bufferSize;
	
	public LineProducer(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	
	public boolean hasRemaining() {
		boolean hasMore = false;
		try {
			reader.mark(1);
			hasMore = reader.read() == -1 ? false : true;
			reader.reset();
		} catch (IOException e) { e.printStackTrace(); }
		return hasMore;
	}
	
	public String get(int lines) {
		StringBuilder builder = new StringBuilder();
		try {
			for (int i = 0; i < lines; i++) {
				builder.append(reader.readLine());
			}
		}
		catch (IOException e) { e.printStackTrace(); }
		return builder.toString();
	}
	
	public void initialize(String filePath) throws FileNotFoundException {
		FileReader fileReader = new FileReader(filePath);
		this.reader = new BufferedReader(fileReader, bufferSize);
	}
	
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
