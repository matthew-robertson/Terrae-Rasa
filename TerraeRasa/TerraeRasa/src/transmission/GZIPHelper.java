package transmission;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPHelper 
{
	public byte[] compress(Object object) 
			throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
		ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
		objectOut.writeObject(object);
		objectOut.close();
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
	
	public Object expand(byte[] bytes) 
			throws IOException, ClassNotFoundException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		GZIPInputStream gzipIn = new GZIPInputStream(bais);
		ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
		Object object = (Object) objectIn.readObject();
		objectIn.close();
		return object;
	}
	
}

/**
 * 
 * Idea for data compression: 
 * ObjectOutputStream using the socket's output stream
 * 
 * Compress down the objects
 * Write as byte[] 
 * 
 * Read as byte[] 
 * Expand (client side)
 * 
 * Apply
 * 
 * -- Wrap the entire update cycle into one compressed object because GZIP streams cannot be reused.
 * 
 * This may work, we'll see I suppose.
 * 
 * -- In memory compression: http://stackoverflow.com/questions/5934495/implementing-in-memory-compression-for-objects-in-java
 * 
 * 
 * 
 * 
 */