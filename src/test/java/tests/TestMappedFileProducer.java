package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import data_partitioners.FilePartitioner;
import data_producer.MappedFileProducer;

public class TestMappedFileProducer {

	@Test
	public void test() {
		MappedFileProducer producer = new MappedFileProducer(FilePartitioner.MEGABYTE * 20);
		producer.initialize("data\\bookCopies.txt");
		while (producer.fileHasMore()) {
			producer.get(FilePartitioner.KILOBYTE * 100);
		}
		System.out.println("MB read: " + producer.bytesRead / FilePartitioner.MEGABYTE);
		assertEquals(producer.length, producer.bytesRead);
		producer.close();
	}
	
	@Test
	public void testMaxMappedSize() {
		MappedFileProducer producer = new MappedFileProducer(FilePartitioner.GIGABYTE * 2);
		producer.initialize("data\\bookCopies.txt");
		while (producer.fileHasMore()) {
			byte[] bytes = producer.get(FilePartitioner.MEGABYTE * 100);
		}
	}
}
