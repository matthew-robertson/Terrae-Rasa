package io;
import java.util.concurrent.Callable;

import transmission.ChunkExpander;
import transmission.SuperCompressedChunk;

public class CallableReconstructChunk implements Callable<Chunk>
{
	private SuperCompressedChunk compressedChunk;
	
	public CallableReconstructChunk(SuperCompressedChunk chunk)
	{
		System.out.println("Created rec- " + chunk.x);
		this.compressedChunk = chunk;
	}
	
	public Chunk call() throws Exception
	{
		Chunk chunk = ChunkExpander.expandChunk(this.compressedChunk);
		System.out.println("Rebuilt Chunk (Position) : " + this.compressedChunk.x);
		return chunk;
	}
}
