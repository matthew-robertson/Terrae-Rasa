package transmission;

import savable.SavableBlock;
import io.Chunk;
import world.Biome;
import blocks.MinimalBlock;

public class ChunkCompressor 
{
	public SuperCompressedChunk compressChunk(Chunk chunk)
	{
		SuperCompressedChunk compressedChunk = new SuperCompressedChunk();
		compressedChunk.biome = new Biome(chunk.getBiome());
		compressedChunk.light = chunk.getLight();
		compressedChunk.diffuseLight = chunk.diffuseLight;
		compressedChunk.ambientLight = chunk.ambientLight;
		compressedChunk.backWalls = compress(chunk.backWalls);
		compressedChunk.blocks = compress(chunk.blocks);
		compressedChunk.x = chunk.getX();
		compressedChunk.wasChanged = chunk.getChanged();
		compressedChunk.height = chunk.getHeight();
		compressedChunk.lightUpdated = chunk.isLightUpdated();
		compressedChunk.flaggedForLightingUpdate = true;
		
		
		return compressedChunk;
	}

	private SuperCompressedBlock[][] compress(MinimalBlock[][] blocks)
	{
		SuperCompressedBlock[][] compressed = new SuperCompressedBlock[blocks.length][blocks[0].length];
		for(int i = 0; i < compressed.length; i++)
		{
			for(int k = 0; k < compressed[0].length; k++)
			{
				SuperCompressedBlock cblock = new SuperCompressedBlock();
				cblock.bitMap = blocks[i][k].bitMap;
				cblock.id = blocks[i][k].id;
				cblock.metaData = blocks[i][k].metaData;
				cblock.mainInventory = blocks[i][k].mainInventory;
				compressed[i][k] = cblock;			
			}
		}
		return compressed;		
	}
	
}
