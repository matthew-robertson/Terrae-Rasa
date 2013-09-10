package transmission;

import io.Chunk;

import java.io.Serializable;

import blocks.Block;
import blocks.MinimalBlock;

public class ChunkExpander 
		implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static Chunk expandChunk(SuperCompressedChunk compressedChunk)
	{
		Chunk chunk = new Chunk(compressedChunk.biome, compressedChunk.x, compressedChunk.height);
		chunk.light = new float[Chunk.getChunkWidth()][compressedChunk.height];
		chunk.diffuseLight = new float[Chunk.getChunkWidth()][compressedChunk.height];
		chunk.ambientLight = new float[Chunk.getChunkWidth()][compressedChunk.height];
		chunk.backWalls = expand(compressedChunk.backWalls, false);
		chunk.blocks = expand(compressedChunk.blocks, true);
		chunk.setChanged(compressedChunk.wasChanged);
		chunk.setLightUpdated(false);
		chunk.setRequiresAmbientLightingUpdate(true);
		chunk.addLightSources(compressedChunk.lightSources);
		chunk.setRequiresDiffuseApplied(true);
		chunk.weather = compressedChunk.weather;
		return chunk;
	}
	
	private static MinimalBlock[][] expand(SuperCompressedBlock[][] compressed, boolean front)
	{
		MinimalBlock[][] blocks = new MinimalBlock[compressed.length][compressed[0].length];
		if(front)
		{
			for(int i = 0; i < compressed.length; i++)
			{
				for(int k = 0; k < compressed[0].length; k++)
				{
					if(compressed[i][k] == null)
					{
						blocks[i][k] = new MinimalBlock(Block.air);	
					}			
					else
					{
						MinimalBlock block = new MinimalBlock(compressed[i][k]);
						blocks[i][k] = block;	
					}
				}
			}
		}
		else 
		{
			for(int i = 0; i < compressed.length; i++)
			{
				for(int k = 0; k < compressed[0].length; k++)
				{
					if(compressed[i][k] == null)
					{
						blocks[i][k] = new MinimalBlock(Block.backAir);
					}			
					else
					{
						MinimalBlock block = new MinimalBlock(compressed[i][k]);
						blocks[i][k] = block;	
					}
				}
			}
		}
		return blocks;
	}
}
