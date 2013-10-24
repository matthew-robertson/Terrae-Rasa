package server;
import java.util.concurrent.Callable;

import transmission.GZIPHelper;

public class CallableGZIPCompressor implements Callable<byte[]>
{
	private Object uncompressedObect;
	
	public CallableGZIPCompressor(Object objectToCompress)
	{
		this.uncompressedObect = objectToCompress;
	}
	
	public byte[] call() throws Exception 
	{
		GZIPHelper gzipHelper = new GZIPHelper();
		byte[] result = gzipHelper.compress(uncompressedObect);
		return result;
	}	
}
