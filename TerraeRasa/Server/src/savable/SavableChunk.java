package savable;

import java.io.Serializable;

import blocks.Block;

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
public class SavableChunk 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int biomeID;
	public float[][] light;
	public float[][] diffuseLight;
	public float[][] ambientLight;
	public SavableBlock[][] backWalls;
	public SavableBlock[][] blocks;
	public int x;
	public boolean wasChanged;
	public int height;
	public boolean lightUpdated;
	public boolean flaggedForLightingUpdate;
}