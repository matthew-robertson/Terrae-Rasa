package io;
import java.util.concurrent.Callable;

import transmission.ChunkExpander;
import transmission.SuperCompressedChunk;

public class CallableReconstructChunk implements Callable<Chunk>
{
	private SuperCompressedChunk compressedChunk;
	
	public CallableReconstructChunk(SuperCompressedChunk chunk)
	{
		this.compressedChunk = chunk;
	}
	
	public Chunk call() throws Exception
	{
		Chunk chunk = ChunkExpander.expandChunk(this.compressedChunk);
		return chunk;
	}
}
