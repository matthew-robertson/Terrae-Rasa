package transmission;

import io.Chunk;

import java.io.Serializable;

import blocks.MinimalBlock;

public class ChunkExpander 
		implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static Chunk expandChunk(SuperCompressedChunk compressedChunk)
	{
		Chunk chunk = new Chunk(compressedChunk.biome, compressedChunk.x, compressedChunk.height);
		chunk.backWalls = expand(compressedChunk.backWalls);
		chunk.blocks = expand(compressedChunk.blocks);
		chunk.setChanged(compressedChunk.wasChanged);
		chunk.setLightUpdated(false);
		chunk.setFlaggedForLightingUpdate(true);
		return chunk;
	}
	
	private static MinimalBlock[][] expand(SuperCompressedBlock[][] compressed)
	{
		MinimalBlock[][] blocks = new MinimalBlock[compressed.length][compressed[0].length];
		for(int i = 0; i < compressed.length; i++)
		{
			for(int k = 0; k < compressed[0].length; k++)
			{
				MinimalBlock block = new MinimalBlock(compressed[i][k]);
				blocks[i][k] = block;			
			}
		}
		return blocks;
	}
}
