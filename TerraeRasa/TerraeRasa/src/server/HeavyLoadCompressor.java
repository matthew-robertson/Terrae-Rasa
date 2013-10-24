package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Things that will take a long time to GZIP Compress can be defered to this class's threadpool
 * to compress and retrieved later via callable.
 * @author alec
 *
 */
public class HeavyLoadCompressor 
{
	private static final int TOTAL_THREADS = 32;
	private static final ExecutorService threadPool = Executors.newFixedThreadPool(TOTAL_THREADS);
	
	public static Future<byte[]> scheduleRequest(Object object)
	{
		CallableGZIPCompressor compressor = new CallableGZIPCompressor(object);
		Future<byte[]> event = threadPool.submit(compressor);
		return event;
	}	
}
