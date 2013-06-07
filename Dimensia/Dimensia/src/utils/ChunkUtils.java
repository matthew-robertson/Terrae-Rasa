package utils;

import io.Chunk;

import org.lwjgl.opengl.Display;

import entities.EntityLivingPlayer;

import world.World;

/**
 * A basic utility class to help with miscellaneous chunk operations. Currently supports getting the
 * chunks near the player, which will be rendered in the given frame, in the method {@link #getRequiredChunks(World, EntityLivingPlayer)}.
 *
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class ChunkUtils 
{
	/**
	 * Gets the chunks that will be rendered for that player, based on screen resolution.
	 * @param world the world object (containing the player)
	 * @param player the player object to get chunks around
	 * @return the chunks that will be rendered
	 */
	public static Chunk[] getRequiredChunks(World world, EntityLivingPlayer player)
	{
		Chunk[] chunks;
		final int width = (int)((float)(Display.getWidth() / 6) / 1.85);;
		float x = ((player.x - (0.25f * Display.getWidth())) / 6);
		if(x < 0) x = 0;
		if(width + x > world.getWidth()) x = world.getWidth() - width - 1;
			
		int xBegin = (int) (x / Chunk.getChunkWidth());
		int xEnd = (int) ((x + width) / Chunk.getChunkWidth());
		chunks = new Chunk[(xEnd - xBegin > 0) ? xEnd - xBegin + 1 : 1];
		
		xEnd++;
		
		int k = 0;
				
		for(int i = xBegin; i < xEnd; i++)
		{
			chunks[k] = world.getChunk(i);
			k++;
		}
		
		return chunks;
	}
}
