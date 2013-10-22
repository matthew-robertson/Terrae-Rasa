package transmission;



import java.io.Serializable;

import world.Weather;
import blocks.Chunk;
import blocks.ChunkClient;
import blocks.ClientMinimalBlock;

public class ChunkExpander 
		implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static ChunkClient expandChunk(SuperCompressedChunk compressedChunk)
	{
		ChunkClient chunk = new ChunkClient(compressedChunk.biome, compressedChunk.x, compressedChunk.height);
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
		if(compressedChunk.weatherData != null)
		{
			chunk.weather = Weather.generateWeatherByID(compressedChunk.weatherData);
		}
		return chunk;
	}
	
	private static ClientMinimalBlock[][] expand(SuperCompressedBlock[][] compressed, boolean front)
	{
		ClientMinimalBlock[][] blocks = new ClientMinimalBlock[compressed.length][compressed[0].length];
		if(front)
		{
			for(int i = 0; i < compressed.length; i++)
			{
				for(int k = 0; k < compressed[0].length; k++)
				{
					if(compressed[i][k] == null)
					{
						blocks[i][k] = new ClientMinimalBlock(true);	
					}			
					else
					{
						ClientMinimalBlock block = new ClientMinimalBlock(compressed[i][k]);
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
						blocks[i][k] = new ClientMinimalBlock(false);
					}			
					else
					{
						ClientMinimalBlock block = new ClientMinimalBlock(compressed[i][k]);
						blocks[i][k] = block;	
					}
				}
			}
		}
		return blocks;
	}
}
