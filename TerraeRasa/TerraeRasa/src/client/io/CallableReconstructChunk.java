package client.io;

import java.util.concurrent.Callable;

import blocks.ChunkClient;

import transmission.ChunkExpander;
import transmission.SuperCompressedChunk;

public class CallableReconstructChunk implements Callable<ChunkClient>
{
	private SuperCompressedChunk compressedChunk;
	
	public CallableReconstructChunk(SuperCompressedChunk chunk)
	{
		this.compressedChunk = chunk;
	}
	
	public ChunkClient call() throws Exception
	{
		ChunkClient chunk = ChunkExpander.expandChunk(this.compressedChunk);
		return chunk;
	}
}
