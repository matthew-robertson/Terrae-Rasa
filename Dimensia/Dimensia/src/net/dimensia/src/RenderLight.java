package net.dimensia.src;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class RenderLight extends Render
{	
	public void render(World world, EntityLivingPlayer player)
	{
		adjustCameraToLastPosition();
		final int width = (int)(Display.getWidth() / 11.1);
		final int height = (int)(Display.getHeight() / 11.1) + 2;
		int x = (int) ((player.x - (0.25f * Display.getWidth())) / 6);
		int y = (int) ((player.y - (0.25f * Display.getHeight())) / 6);
		if(y < 0) y = 0;
		if(x < 0) x = 0;
		if(width + x > world.getWidth()) x = world.getWidth() - width;
		if(height + y > world.getHeight()) y = world.getHeight() - height - 1;
		Chunk[] chunks = ChunkUtils.getRequiredChunks(world, player);
				
		//Settings for lighting
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		//'base' render position (this is a constant offset that needs applied). It is the edge of a chunk, in Blocks, generally the nearest left or up.
		final int baseX_F = (int) ((int)(x / Chunk.getChunkWidth()) * Chunk.getChunkWidth());
		//set the amount of remaining blocks, initially (this is reset later)
		int remainingWidth = width;
		int remainingHeight = height;
		
		if(player.getHandheldLight() != null)
		{
			BlockLight block = (BlockLight)(player.getHandheldLight());
			LightUtils lightUtils = new LightUtils();
			float[][] lightMap = lightUtils.getLightMap(world, 
					((int)(player.x / 6)) + ((player.isFacingRight) ? 1 : 0), 
					((int)(player.y / 6)) + 1, 
					block.lightRadius, 
					block.lightStrength);
			
			int playerX = (int) ((player.x / 6) + + ((player.isFacingRight) ? 1 : 0));
			int playerY = (int) ((player.y / 6) + 1);
			int radius = block.lightRadius;
			
			t.startDrawingQuads();
			for(int i = 0; i < chunks.length; i++) //Loop each X Chunk
			{
				remainingHeight = height;
				int baseX = baseX_F + (i * Chunk.getChunkWidth());
				int xoff = 0;
				int loopX = 0;
				
				//Determine the offset to start rendering, and how far to render
				//Also ensure rendering stays in bounds. This is for the X rendering calculations
				if(i == 0) //First Chunk
				{
					xoff = (int) (x % Chunk.getChunkWidth());
					loopX = (int) (Chunk.getChunkWidth() - (x % Chunk.getChunkWidth()));
					remainingWidth -= loopX;
					if(remainingWidth < 0) 
					{
						loopX += remainingWidth;
					}
				}					
				else if(i == chunks.length - 1 && i != 0) //Last Chunk (may never get hit)
				{
					xoff = 0;
					loopX = remainingWidth;
					remainingWidth = 0;
				}
				else //Not tested. Should allow an entire 'middle' chunk to render (this requires something stupid like 1800x3600 resolution to hit)
				{
					xoff = 0;
					loopX = Chunk.getChunkWidth();
					remainingWidth -= loopX;
				}
				
				int yoff = 0;
				int loopY = 0;
				
				//Determine the offset to start rendering, and how far to render
				//Also ensure rendering stays in bounds. This is for the Y rendering calculations
				yoff = (int) (y % world.getHeight());
				loopY = (int) (world.getHeight() - (y % world.getHeight()));
				remainingHeight -= loopY;
				if(remainingHeight < 0) 
				{
					loopY += remainingHeight;
				}
				
				//Fix random blocks not rendering. Generally occurs on the edge of chunks
				if(xoff + loopX < Chunk.getChunkWidth())
				{
					loopX++;
				}
				if(yoff + loopY < world.getHeight()) 
				{
					loopY++;
				}
				if((int)(y) == world.getHeight()) 
				{
					y++;
				}
	
				int chunkXOffset = baseX_F + (i * Chunk.getChunkWidth());
				
				for(int k = xoff; k < xoff + loopX; k++) //x
				{	
					for(int l = yoff; l < yoff + loopY; l++) //y
					{						
						if(k + (chunkXOffset) > (playerX - radius) && k + (chunkXOffset) < (playerX + radius) 
								&& l > (playerY - radius) && l < (playerY + radius))
						{
							int x1 = k + baseX;
							int y1 = l;							
							int xm = x1 * BLOCK_SIZE; 
							int ym = y1 * BLOCK_SIZE; 
							
							float torchLight = lightMap[chunkXOffset + k - (playerX - radius)][l - (playerY - radius)];
							float chunkLight = chunks[i].light[k][l];
							float newLight = (chunkLight - torchLight >= 0.0F) ? chunks[i].light[k][l] - torchLight : 0.0F;
														
							t.setColorRGBA(0, 0, 0, (int)(newLight * 255F));
							t.addVertexWithUV(xm, BLOCK_SIZE + ym, 0, 0, 0.03125);
							t.addVertexWithUV(xm + BLOCK_SIZE, BLOCK_SIZE + ym, 0, 0.0625, 0.03125);
							t.addVertexWithUV(xm + BLOCK_SIZE, ym, 0, 0.0625, 0);
							t.addVertexWithUV(xm, ym, 0, 0, 0);
						}
						else
						{	
							int x1 = k + baseX;
							int y1 = l;							
							int xm = x1 * BLOCK_SIZE; 
							int ym = y1 * BLOCK_SIZE; 
							
							t.setColorRGBA(0, 0, 0, (int)(chunks[i].light[k][l] * 255F));
							t.addVertexWithUV(xm, BLOCK_SIZE + ym, 0, 0, 0.03125);
							t.addVertexWithUV(xm + BLOCK_SIZE, BLOCK_SIZE + ym, 0, 0.0625, 0.03125);
							t.addVertexWithUV(xm + BLOCK_SIZE, ym, 0, 0.0625, 0);
							t.addVertexWithUV(xm, ym, 0, 0, 0);
						}
					}
				}
				
				if((int)(x) == Chunk.getChunkWidth())
				{
					x++;
				}
			}
			t.draw();
		}
		else
		{
			t.startDrawingQuads();
			for(int i = 0; i < chunks.length; i++) //Loop each X Chunk
			{
				remainingHeight = height;
				int baseX = baseX_F + (i * Chunk.getChunkWidth());
				int xoff = 0;
				int loopX = 0;
				
				//Determine the offset to start rendering, and how far to render
				//Also ensure rendering stays in bounds. This is for the X rendering calculations
				if(i == 0) //First Chunk
				{
					xoff = (int) (x % Chunk.getChunkWidth());
					loopX = (int) (Chunk.getChunkWidth() - (x % Chunk.getChunkWidth()));
					remainingWidth -= loopX;
					if(remainingWidth < 0) 
					{
						loopX += remainingWidth;
					}
				}					
				else if(i == chunks.length - 1 && i != 0) //Last Chunk (may never get hit)
				{
					xoff = 0;
					loopX = remainingWidth;
					remainingWidth = 0;
				}
				else //Not tested. Should allow an entire 'middle' chunk to render (this requires something stupid like 1800x3600 resolution to hit)
				{
					xoff = 0;
					loopX = Chunk.getChunkWidth();
					remainingWidth -= loopX;
				}
								
				int yoff = 0;
				int loopY = 0;
				
				//Determine the offset to start rendering, and how far to render
				//Also ensure rendering stays in bounds. This is for the Y rendering calculations
				yoff = (int) (y % world.getHeight());
				loopY = (int) (world.getHeight() - (y % world.getHeight()));
				remainingHeight -= loopY;
				if(remainingHeight < 0) 
				{
					loopY += remainingHeight;
				}
				
				//Fix random blocks not rendering. Generally occurs on the edge of chunks
				if(xoff + loopX < Chunk.getChunkWidth())
				{
					loopX++;
				}
				if(yoff + loopY < world.getHeight()) 
				{
					loopY++;
				}
				if((int)(y) == world.getHeight()) 
				{
					y++;
				}
	
				for(int k = xoff; k < xoff + loopX; k++) //x
				{	
					for(int l = yoff; l < yoff + loopY; l++) //y
					{		
						int x1 = k + baseX;
						int y1 = l;
						int xm = x1 * BLOCK_SIZE; 
						int ym = y1 * BLOCK_SIZE; 
						
						t.setColorRGBA(0, 0, 0, (int)(chunks[i].light[k][l] * 255F));
						t.addVertexWithUV(xm, BLOCK_SIZE + ym, 0, 0, 0.03125);
						t.addVertexWithUV(xm + BLOCK_SIZE, BLOCK_SIZE + ym, 0, 0.0625, 0.03125);
						t.addVertexWithUV(xm + BLOCK_SIZE, ym, 0, 0.0625, 0);
						t.addVertexWithUV(xm, ym, 0, 0, 0);
					}
				}
				
				if((int)(x) == Chunk.getChunkWidth())
				{
					x++;
				}
			}
			t.draw();
		}
					
		GL11.glEnable(GL11.GL_TEXTURE_2D);		
	}
}
