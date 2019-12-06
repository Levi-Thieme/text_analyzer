package jobs;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import data_producer.DataProducer;

public class WordCount implements Job<Object>{
	private DataProducer<String> producer;
	private String outputPath;
	private String inputPath;
	private int chunkSize;
	private int maxWords;
	private String regex;
	
	public WordCount(DataProducer<String> producer, int chunkSize, String regex, String inputPath, String outputPath) {
		this.producer = producer;
		this.outputPath = outputPath;
		this.inputPath = inputPath;
		this.chunkSize = chunkSize;
		this.regex = regex;
	}
	
	public Object execute() {
		try {
			producer.initialize(this.inputPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DB db = DBMaker
				.memoryDirectDB()
		        .make();
		HTreeMap<String, Long> map = db
		        .hashMap("map", Serializer.STRING, Serializer.LONG)
		        .expireMaxSize(maxWords)
		        .createOrOpen();
		while(producer.hasRemaining()) {
			String text = producer.get(chunkSize);
			text = text.replaceAll("\\p{P}", " ");
			StringTokenizer tokenizer = new StringTokenizer(text);
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken().trim();
				if (Pattern.matches(this.regex, token)) {
					map.compute(token, (word, count) -> count == null ? 1l : count + 1l);
				}
			}
		}
		producer.close();
		writeResults(outputPath, map);
		map.close();
		db.close();
		return null;
	}
	
	private void writeResults(String path, Map<String, Long> results) {
		try (FileWriter writer = new FileWriter(path, false)) {
			for (Entry<String, Long> entry : results.entrySet()) {
				writer.write(entry.getKey() + "," + entry.getValue() + "\n");
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
}
