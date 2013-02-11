package net.dimensia.src;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;

public class CallableSaveChunk implements Callable<Boolean>
{
	private String basepath;
	private String worldName;
	private Chunk chunk;
	private int x;
	private int y;
	
	public CallableSaveChunk(Chunk chunk, int x, int y, String basepath, String worldName)
	{
		this.chunk = chunk;
		this.x = x;
		this.y = y;
		this.basepath = basepath;
		this.worldName = worldName;
	}
	
	public Boolean call() throws Exception 
	{
		String fileName = (basepath + "/" + x + "," + y + ".wdat"); 
		GZIPOutputStream fileWriter = new GZIPOutputStream(new FileOutputStream(new File(fileName))); //Open an output stream
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(); //Convert world to byte[]
		ObjectOutputStream s = new ObjectOutputStream(bos); //open the OOS, used to save serialized objects to file
		s.writeObject(chunk); //write the byte[] to the OOS
		byte data[] = bos.toByteArray();
		fileWriter.write(data, 0, data.length); //Actually save it to file
		System.out.println("Chunk Saved to: " + fileName + " With Initial Size: " + data.length + " X: " + x + " Y: " + y);
		
		//Cleanup: 
		s.close();
		bos.close();
		fileWriter.close();     
		
		return true;
	}
}
