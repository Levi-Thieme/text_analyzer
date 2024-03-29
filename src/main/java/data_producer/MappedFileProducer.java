package data_producer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedFileProducer implements DataProducer<byte[]>{
	private MappedByteBuffer mappedBuffer;
	public long bytesMapped;
	public long bytesRead;
	private RandomAccessFile raf;
	private FileChannel channel;
	public long length;
	
	public MappedFileProducer(long bytesMapped) {
		this.bytesMapped = bytesMapped;
		this.bytesRead = 0;
		this.length = 0;
	}
	
	/**
	 * Initializes this.mappedBuffer with a reference to a MappedByteBuffer.
	 */
	public void initialize(String filePath) {
		try {
            this.raf = new RandomAccessFile(filePath, "r");
            this.channel = raf.getChannel();
            this.length = raf.length();
        } catch(Exception e) {e.printStackTrace();}
		this.loadBuffer();
	}
	
	/**
	 * Closes the filechannel and its channel.
	 */
	public void close() {
		try {
			this.channel.close();
			this.raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return true if the file has more bytes to process.
	 */
	public boolean fileHasMore() {
		return this.length - this.bytesRead > 0;
	}
	
	/**
	 * 
	 * @return true if there are remaining bytes in this.mappedBuffer.
	 * @throws IOException 
	 */
	public boolean hasRemaining() {
		return this.mappedBuffer.hasRemaining();
	}
	
	/**
	 * Gets a "chunk" of the mappedBuffer.
	 * @param size The number of bytes to retrieve.
	 * @return A byte[]
	 */
	public byte[] get(int size) {
		byte[] bytes = new byte[size];
		if (this.mappedBuffer.hasRemaining()) {
			//System.out.println("getting chunk");
			int toRead = Math.min(size, this.mappedBuffer.remaining());
			mappedBuffer.get(bytes, 0, toRead);
			this.bytesRead += toRead;
		}
		else if (this.fileHasMore()) {
			System.out.println("loading buffer");
			this.loadBuffer();
			bytes = this.get(size);
		}
		return bytes;
	}
	
	/**
	 * Sets this.mappedBuffer to a new memory mapped buffer starting at this.bytesRead,
	 * and ending at Math.min(length, freeBytes / 2) where freeBytes is the amount of free RAM
	 * available to the JVM.
	 */
	private boolean loadBuffer() {
		try {
			if (this.fileHasMore()) {
				long remainingBytes = this.length - this.bytesRead;
				long bytesToMap = Math.min(Math.min(this.bytesMapped, remainingBytes), Integer.MAX_VALUE);
				System.out.println(remainingBytes + "\n" + bytesToMap);
				this.mappedBuffer = this.channel.map(FileChannel.MapMode.READ_ONLY, this.bytesRead, bytesToMap);
				this.mappedBuffer.load();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}	
}








































