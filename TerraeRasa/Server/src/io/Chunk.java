package io;

import java.util.Iterator;
import java.util.Vector;

import utils.Position;
import world.Biome;
import blocks.Block;
import blocks.MinimalBlock;

/**
 * <br>
 * <code>Chunk</code> implements something that is similar to a C(++) struct. A Chunk stores data relating to
 * Backwalls, Blocks, Biomes, and lighting data(including ambient, diffuse, and total). Upon initialization, 
 * all Blocks and Backwalls are set to be air, a Biome must be assigned, and lighting values are set to 0. 
 * Additionally, each chunk stores its own position in the chunk array. 
 * <br><br>
 * Each method in <code>Chunk</code> is either synchronized or final to make <code>Chunk</code> 
 * relatively Thread-Safe overall. All setters (for example: <code>{@link #setBlock(Block, int, int)}, 
 * {@link #setChanged(boolean)}, {@link #setLight(double, int, int)}</code>) are synchronized, all getters are 
 * final. All fields in <code>Chunk</code> are final.
 * 
 * <br><br>
 * <b>Chunk sizes are subject to change. NEVER use magic numbers and always use 
 * {@link #getChunkWidth()} or {@link #getChunkHeight()} when performing chunk
 * size operations.</b>
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.1
 * @since       1.0
 */
public class Chunk 
{
	private Biome biome;
	public MinimalBlock[][] backWalls;
	public MinimalBlock[][] blocks;
	private final int x;
	private boolean wasChanged;
	private static final int CHUNK_WIDTH = 100;
	private final int height;
	private Vector<Position> lightSources;
	
	/**
	 * Constructs a new Chunk. Chunks are initialized with blocks[][] fully set to air, and backwalls[][]
	 * fully set to air as well. All light values are 0.0f (no light).
	 * @param biome the biome type of the chunk
	 * @param x the x position of the chunk in the chunk grid
	 * @param y the y position of the chunk in the chunk grid
	 * @param height the height of the chunk - which should be the world's height
	 */
	public Chunk(Biome biome, int x, final int height)
	{
		this.height = height;
		this.biome = new Biome(biome);
		blocks = new MinimalBlock[CHUNK_WIDTH][height];
		backWalls = new MinimalBlock[CHUNK_WIDTH][height];
		for(int i = 0; i < CHUNK_WIDTH; i++)
		{
			for(int j = 0; j < height; j++)
			{
				blocks[i][j] = new MinimalBlock(Block.air);
				backWalls[i][j] = new MinimalBlock(Block.backAir);
			}
		}
		
		this.x = x;
		this.lightSources = new Vector<Position>();
	}
	
	/**
	 * Gets the final CHUNK_WIDTH, the block width of all chunks. 
	 * @return CHUNK_WIDTH, the width of all chunks
	 */
	public static final int getChunkWidth()
	{
		return CHUNK_WIDTH;
	}
	
	/**
	 * Gets the height of this chunk, the height of a chunk can change based on the world size selected. It should be the 
	 * world's height.
	 * @return the height of this chunk
	 */
	public final int getHeight()
	{
		return height;
	}
	
	/**
	 * Gets the biome for this specific chunk.
	 * @return the biome of this chunk
	 */
	public final Biome getBiome()
	{
		return biome;
	}
	
	/**
	 * Sets the biome of the chunk to the specified new biome type, creating a deep copy of the biome.
	 * @param biome the new biome type assigned to this chunk
	 */
	public synchronized void setBiome(Biome biome)
	{
		this.biome = new Biome(biome);
	}
	
	/**
	 * Gets the block at position (x,y) of the chunk, NOT the world 
	 * @param x the x position of the block requested
	 * @param y the y position of the block requested
	 * @return the block at the specified position, which should never be null
	 */
	public final MinimalBlock getBlock(int x, int y)
	{
		return blocks[x][y];
	}
	
	/**
	 * Gets the backwall at position (x,y) of the chunk, NOT the world 
	 * @param x the x position of the block requested
	 * @param y the y position of the block requested
	 * @return the block at the specified position, which should never be null
	 */
	public final MinimalBlock getBackWall(int x, int y)
	{
		return backWalls[x][y];
	}
		
	/**
	 * Gets whether or not this Chunk has been flagged as having been changed, for some reason. This is generally not very descriptive
	 * and may not even happen at all. 
	 * @return whether or not this Chunk has been changed
	 */
	public final boolean getChanged()
	{
		return wasChanged;
	}
	
	/**
	 * Replaces the current block at backWalls[x][y] with the given Block parameter.
	 * @param block the new Block for position (x,y)
	 * @param x a value from 0 to ChunkWidth	
	 * @param y a value from 0 to ChunkHeight
	 */
	public synchronized void setBackWall(Block block, int x, int y)
	{
		backWalls[x][y] = new MinimalBlock(block.clone());
	}
	
	/**
	 * Replaces the current block at blocks[x][y] with the given Block parameter.
	 * @param block the new Block for position (x,y)
	 * @param x a value from 0 to ChunkWidth	
	 * @param y a value from 0 to ChunkHeight
	 */
	public synchronized void setBlock(Block block, int x, int y)
	{
		if(Block.blocksList[blocks[x][y].id].lightStrength > 0)
		{
			removeLightSource(x, y);
		}
		if(block.lightStrength > 0)
		{
			addLightSource(x, y);
		}
		blocks[x][y] = new MinimalBlock(block.clone());
	}
		
	/**
	 * Registers a light source at the given position.
	 * @param x the x position, in blocks, relative to the start of this chunk (IE a value 0 <= x < Chunk_width)
	 * @param y the y position, in blocks
	 */
	private void addLightSource(int x, int y)
	{
		lightSources.add(new Position((this.x * CHUNK_WIDTH) + x, y));
	}
	
	/**
	 * Removes a light source at the given position. This will make the game mad if it fails to locate the given light source.
	 * @param x the x position, in blocks, relative to the start of this chunk (IE a value 0 <= x < Chunk_width)
	 * @param y the y position, in blocks
	 */
	private void removeLightSource(int x, int y)
	{
		int adjustedX = this.x * CHUNK_WIDTH + x;
		//(adjustedX,y)
		Iterator<Position> it = lightSources.iterator();
		while(it.hasNext())
		{
			Position position = it.next();
			if(position.equals(adjustedX, y))
			{
				it.remove();
				return;
			}
		}		
		throw new RuntimeException("Illegal light source removal at (" + (adjustedX) + "," + y + ")");
	}
	
	/**
	 * Sets the wasChanged variable of this chunk to the given boolean
	 * @param flag the new value for this Chunk's wasChanged field
	 */
	public synchronized void setChanged(boolean flag)
	{
		wasChanged = flag;
	}
		
	/**
	 * Gets the x position of this Chunk, in the chunk map
	 * @return the x position of this Chunk in the chunk map
	 */
	public final int getX()
	{
		return x;
	}

	public Vector<Position> getLightPositions()
	{
		return lightSources;
	}

	public Position[] getLightSourcesAsArray() {
		Position[] positions = new Position[lightSources.size()];
		lightSources.copyInto(positions);
		return positions;
	}
}