package io;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.Callable;
import java.util.zip.GZIPInputStream;

public class CallableLoadChunk implements Callable<Chunk>
{
	private String basepath;
	private int x;
	private ChunkManager manager;
	
	public CallableLoadChunk(ChunkManager manager, int x, String basepath, String worldName)
	{
		this.x = x;
		this.basepath = basepath;
		this.manager = manager;
	}
	
	public Chunk call() throws Exception
	{
		String fileName = basepath + "/" + x + ".wdat";
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))); //Open an input stream
		Chunk chunk = (Chunk)ois.readObject(); //Load the object
		System.out.println("Chunk Loaded From File Path : " + fileName);
		ois.close();
		manager.unlockChunk(chunk.getX());
		return chunk;
	}
	
	public String getBasePath()
	{
		return basepath;
	}
}
