package net.dimensia.src;
import java.io.Serializable;

/**
 * <code>Chunk implements Serializable</code>
 * <br>
 * <code>Chunk</code> implements something that is similar to a C(++) struct. It stores data relating to
 * back walls, Blocks, and lighting values. Upon initialization, all Blocks and backwalls are set to be air, 
 * not null, due to nullpointer issues that were previously occuring. Additionally, each chunk stores
 * its own position in the chunk array, should it be requested. 
 * <br><br>
 * Each method in <code>Chunk</code> is either synchronized or final to make <code>Chunk</code> 
 * relatively Thread-Safe overall. All setters (<code>{@link #setBlock(Block, int, int)}, 
 * {@link #setChanged(boolean)}, {@link #setLight(float, int, int)}</code>) are synchronized, all getters are 
 * final. All fields in <code>Chunk</code> are final.
 * 
 * <br><br>
 * <b>Chunk sizes are subject to change. NEVER use "magic numbers" and always use 
 * {@link #getChunkWidth()} or {@link #getChunkHeight()} when performing chunk
 * size operations.</b>
 * 
 * <i> Update: </i> Chunks now store biome data, which can be set with 
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Chunk 
		implements Serializable
{
	/** V2 includes lighting */
	private static final long serialVersionUID = 2L;
	private Biome biome;
	public final float[][] light;
	public final float[][] diffuseLight;
	public final float[][] ambientLight;
	public final Block[][] backWalls;
	public final Block[][] blocks;
	private final int x;
	private boolean wasChanged;
	private static final int CHUNK_WIDTH = 100;
	private final int CHUNK_HEIGHT;
	public boolean lightUpdated;
	
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
		//this.biome = biome;
		this.biome = Biome.forest;
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
		
		light = new float[CHUNK_WIDTH][CHUNK_HEIGHT];
		diffuseLight = new float[CHUNK_WIDTH][CHUNK_HEIGHT];
		ambientLight = new float[CHUNK_WIDTH][CHUNK_HEIGHT];
		this.x = x;
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
	 * Gets the height of this chunk, the height of a chunk can change based on the world size selected.
	 * @return the height of this chunk
	 */
	public final int getChunkHeight()
	{
		return CHUNK_HEIGHT;
	}
	
	public final Biome getBiome()
	{
		return biome;
	}
	
	/**
	 * Sets the biome of the chunk to the specified new biome type
	 * @param biome the new biome type assigned to this chunk
	 */
	public synchronized void setBiome(Biome biome)
	{
		this.biome = biome;
	}
	
	/**
	 * Gets the light[][] stored in this instanceof Chunk
	 * @return the light array for this Chunk
	 */
	public final float[][] getLight()
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
	
	public final float getLight(int x, int y)
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
	
	public synchronized void setBlock(Block block, int x, int y)
	{
		blocks[x][y] = block.clone();
	}
	
	public synchronized void setLight(float strength, int x, int y)
	{
		light[x][y] = strength;
	}
	
	public synchronized void setDiffuseLight(float strength, int x, int y)
	{
		diffuseLight[x][y] = strength;
	}
	
	public synchronized void setAmbientLight(float strength, int x, int y)
	{
		ambientLight[x][y] = strength;
	}
	
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
	
	public void printLight()
	{
		for(int i = 0; i < CHUNK_WIDTH; i++)
		{
			for(int j = 0; j < CHUNK_HEIGHT; j++)
			{
				System.out.printf("%4.2f", light[i][j]);
			}
			System.out.println();
		}
	}
	
	public float getDiffuseLight(int x, int y)
	{
		return diffuseLight[x][y];
	}
	
	public float getAmbientLight(int x, int y)
	{
		return ambientLight[x][y];
	}
	
	public void updateChunkLight()
	{
		for(int i = 0; i < CHUNK_WIDTH; i++)
		{
			for(int k = 0; k < CHUNK_HEIGHT; k++)
			{
				light[i][k] = MathHelper.sat(1.0F - ambientLight[i][k] - diffuseLight[i][k]);
			}
		}	
	}
	
	public void clearAmbientLight()
	{
		for(int i = 0; i < CHUNK_WIDTH; i++)
		{
			for(int k = 0; k < CHUNK_HEIGHT; k++)
			{
				ambientLight[i][k] = 0.0F;//MathHelper.sat(1.0F - ambientLight[i][k] - diffuseLight[i][k]);
			}
		}	
	}	
}