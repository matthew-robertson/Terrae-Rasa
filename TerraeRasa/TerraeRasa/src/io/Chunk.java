package io;

import java.io.Serializable;

import utils.MathHelper;
import world.Biome;

import blocks.Block;

/**
 * <code>Chunk implements Serializable</code>
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
		implements Serializable
{
	/** V2 includes lighting */
	private static final long serialVersionUID = 2L;
	private Biome biome;
	/** Light is the total of ambient and diffuse light. This value is inverted (0.0F becomes 1.0F, etc) to optimize rendering */
	public final double[][] light;
	/** Diffuse light is light from light sources*/
	public final double[][] diffuseLight;
	/** Ambient light is light from the sun */
	public final double[][] ambientLight;
	public final Block[][] backWalls;
	public final Block[][] blocks;
	private final int X;
	private boolean wasChanged;
	private static final int CHUNK_WIDTH = 100;
	private final int CHUNK_HEIGHT;
	private boolean lightUpdated;
	private boolean flaggedForLightingUpdate;
	
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
		this.CHUNK_HEIGHT = height;
		this.biome = new Biome(biome);
		blocks = new Block[CHUNK_WIDTH][CHUNK_HEIGHT];
		backWalls = new Block[CHUNK_WIDTH][CHUNK_HEIGHT];
		for(int i = 0; i < CHUNK_WIDTH; i++)
		{
			for(int j = 0; j < CHUNK_HEIGHT; j++)
			{
				blocks[i][j] = Block.air;
				backWalls[i][j] = Block.backAir;
			}
		}
		
		setFlaggedForLightingUpdate(false);
		light = new double[CHUNK_WIDTH][CHUNK_HEIGHT];
		diffuseLight = new double[CHUNK_WIDTH][CHUNK_HEIGHT];
		ambientLight = new double[CHUNK_WIDTH][CHUNK_HEIGHT];
		this.X = x;
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
	public final int getChunkHeight()
	{
		return CHUNK_HEIGHT;
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
	 * Gets the light[][] stored in this instanceof Chunk
	 * @return the light array for this Chunk
	 */
	public final double[][] getLight()
	{
		return light;
	}
	
	/**
	 * Gets the block at position (x,y) of the chunk, NOT the world 
	 * @param x the x position of the block requested
	 * @param y the y position of the block requested
	 * @return the block at the specified position, which should never be null
	 */
	public final Block getBlock(int x, int y)
	{
		return blocks[x][y];
	}
	
	/**
	 * Gets the backwall at position (x,y) of the chunk, NOT the world 
	 * @param x the x position of the block requested
	 * @param y the y position of the block requested
	 * @return the block at the specified position, which should never be null
	 */
	public final Block getBackWall(int x, int y)
	{
		return backWalls[x][y];
	}
	
	/**
	 * Gets the currently calculated light value for the block at position (x,y) in the chunk. A value of
	 * 1.0F is full darkness, 0.0F is full light (this reversal is an optimization for rendering)
	 * @param x a value from 0 to ChunkWidth, to retrieve from the light[][]
	 * @param y a value from 0 to ChunkHeight, to retrieve from the light[][]
	 * @return the currently calculated light value of light[x][y]
	 */
	public final double getLight(int x, int y)
	{
		return light[x][y];
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
		backWalls[x][y] = block.clone();
	}
	
	/**
	 * Replaces the current block at blocks[x][y] with the given Block parameter.
	 * @param block the new Block for position (x,y)
	 * @param x a value from 0 to ChunkWidth	
	 * @param y a value from 0 to ChunkHeight
	 */
	public synchronized void setBlock(Block block, int x, int y)
	{
		blocks[x][y] = block.clone();
	}
	
	/**
	 * Sets the diffuseLight[x][y] value to the given strength. 
	 * @param strength a value from 0.0F to 1.0F, indicating the % of diffuse light strength
	 * @param x a value from 0 to ChunkWidth	
	 * @param y a value from 0 to ChunkHeight
	 */
	public synchronized void setDiffuseLight(double strength, int x, int y)
	{
		diffuseLight[x][y] = strength;
	}
	
	/**
	 * Sets the ambientLight[x][y] value to the given strength. 
	 * @param strength a value from 0.0F to 1.0F, indicating the % of ambient light strength
	 * @param x a value from 0 to ChunkWidth	 
	 * @param y a value from 0 to ChunkHeight
	 */
	public synchronized void setAmbientLight(double strength, int x, int y)
	{
		ambientLight[x][y] = strength;
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
		return X;
	}
	
	/**
	 * Gets the diffuse (artificial) light value from diffuseLight[x][y]. A value of 1.0F is full light, 
	 * 0.0F is no light (This value may exceed 1.0F, in which case it is simply full light).
	 * @param x a value from 0 to ChunkWidth
	 * @param y a value from 0 to ChunkHeight
	 * @return the diffuse light value from diffuseLight[x][y]
	 */
	public final double getDiffuseLight(int x, int y)
	{
		return diffuseLight[x][y];
	}

	/**
	 * Gets the ambient(sun) light value from ambientLight[x][y]. A value of 1.0F is full light, 
	 * 0.0F is no light (This value may not 1.0F, unlike diffuse light values).
	 * @param x a value from 0 to ChunkWidth
	 * @param y a value from 0 to ChunkHeight
	 * @return the diffuse light value from ambientLight[x][y]
	 */
	public final double getAmbientLight(int x, int y)
	{
		return ambientLight[x][y];
	}
	
	/**
	 * Updates the light[][] for rendering. A value of 0.0F is full light, 1.0F is full darkness. This is inverted 
	 * for optimization reasons.
	 */
	public synchronized void updateChunkLight()
	{
		for(int i = 0; i < CHUNK_WIDTH; i++)
		{
			for(int k = 0; k < CHUNK_HEIGHT; k++)
			{
				light[i][k] = MathHelper.sat(1.0F - ambientLight[i][k] - diffuseLight[i][k]);
			}
		}	
	}
	
	/**
	 * Sets all the ambientlight[][] to 0.0F (no light)
	 */
	public synchronized void clearAmbientLight()
	{
		for(int i = 0; i < CHUNK_WIDTH; i++)
		{
			for(int k = 0; k < CHUNK_HEIGHT; k++)
			{
				ambientLight[i][k] = 0.0F;
			}
		}	
	}

	/**
	 * Gets the Chunk's flaggedForLightUpdate field
	 * @return this Chunk's flaggedForLightUpdate field
	 */
	public final boolean isFlaggedForLightingUpdate() 
	{
		return flaggedForLightingUpdate;
	}

	/**
	 * Sets this Chunk's flaggedForLightingUpdate field to the given boolean
	 * @param flaggedForLightingUpdate the new value for this Chunk's flaggedForLightingUpdate field
	 */
	public synchronized void setFlaggedForLightingUpdate(boolean flaggedForLightingUpdate) 
	{
		this.flaggedForLightingUpdate = flaggedForLightingUpdate;
	}

	/**
	 * Gets whether or not the light was updated
	 * @return a boolean describing whether or not the light has been updated
	 */
	public final boolean isLightUpdated() 
	{
		return lightUpdated;
	}

	/**
	 * Sets this Chunk's lightUpdated field to the given boolean
	 * @param lightUpdated the new value for this Chunk's lightUpdated field
	 */
	public synchronized void setLightUpdated(boolean lightUpdated) 
	{
		this.lightUpdated = lightUpdated;
	}	
}