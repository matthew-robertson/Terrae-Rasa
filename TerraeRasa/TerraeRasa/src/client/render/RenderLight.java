package client.render;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import blocks.Block;
import blocks.Chunk;
import blocks.ChunkClient;
import client.entities.EntityPlayer;
import client.utils.ChunkUtils;
import client.utils.HeldLightMap;
import client.utils.LightUtils;
import client.world.WorldClientEarth;

public class RenderLight extends Render
{	
	public void render(WorldClientEarth world, List<EntityPlayer> players, EntityPlayer activePlayer)
	{
		try {
			//Initialize the variables for the screen bounds, using the active player from the client.
			adjustCameraToLastPosition();
			final int width = (int)(Display.getWidth() / 11.1);
			final int height = (int)(Display.getHeight() / 11.1) + 6;
			int baseRenderX = (int) ((activePlayer.x - (0.25f * Display.getWidth())) / 6);
			int baseRenderY = (int) ((activePlayer.y - (0.25f * Display.getHeight())) / 6);
			if(baseRenderY < 0) baseRenderY = 0;
			if(baseRenderX < 0) baseRenderX = 0;
			if(width + baseRenderX > world.getWidth()) baseRenderX = world.getWidth() - width;
			if(height + baseRenderY > world.getHeight()) baseRenderY = world.getHeight() - height - 1;
			ChunkClient[] chunks = ChunkUtils.getRequiredChunks(world, activePlayer);
					
			//Settings for lighting
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			//'base' render position (this is a constant offset that needs applied). It is the edge of a chunk, in Blocks, generally the nearest left or up.
			final int baseX_F = (int) ((int)(baseRenderX / Chunk.getChunkWidth()) * Chunk.getChunkWidth());
			//set the amount of remaining blocks, initially (this is reset later)
			int remainingWidth = width;
			int remainingHeight = height;
	
			//Generate the lightmaps
			LightUtils lightUtils = new LightUtils();
			List<HeldLightMap> lightMaps = new ArrayList<HeldLightMap>();
			Iterator<EntityPlayer> it = players.iterator();
			while(it.hasNext())
			{
				EntityPlayer player = it.next();
				Block block = player.getHandheldLight();
				if(block != null)
				{
					try {
						HeldLightMap map = new HeldLightMap(((int)(player.x / 6)) + ((player.isFacingRight) ? 1 : 0), 
								((int)(player.y / 6)) + 1, 
								block.lightRadius, 
								lightUtils.getLightMap(world, 
										((int)(player.x / 6)) + ((player.isFacingRight) ? 1 : 0), 
										((int)(player.y / 6)) + 1, 
										block.lightRadius, 
										block.lightStrength)
								);
						lightMaps.add(map);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
				
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
					xoff = (int) (baseRenderX % Chunk.getChunkWidth());
					loopX = (int) (Chunk.getChunkWidth() - (baseRenderX % Chunk.getChunkWidth()));
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
				yoff = (int) (baseRenderY % world.getHeight());
				loopY = (int) (world.getHeight() - (baseRenderY % world.getHeight()));
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
				if((int)(baseRenderY) == world.getHeight()) 
				{
					baseRenderY++;
				}
	
				int chunkXOffset = baseX_F + (i * Chunk.getChunkWidth());
				
				for(int x = xoff; x < xoff + loopX; x++) //x
				{	
					for(int y = yoff; y < yoff + loopY; y++) //y
					{			
						double chunkLight = chunks[i].light[x][y];
						double light = chunkLight;
						double torchLight = 0;
						
						for(HeldLightMap map : lightMaps)
						{
							if(chunkXOffset + x > map.x - map.radius &&
									chunkXOffset + x < map.x + map.radius &&
									y > map.y - map.radius &&
									y < map.y + map.radius
									)
							{
								torchLight += map.lightmap[chunkXOffset + x - (map.x - map.radius)][y - (map.y - map.radius)];
							}
								
								
							
							
	//						if(
	//								
	//								x + (chunkXOffset) > (playerX - radius) && x + (chunkXOffset) < (playerX + radius) 
	//								&& y > (playerY - radius) && y < (playerY + radius)
	//								
	//								
	//								)
	//						{
	
								
								
								
								
	
								
								
	//						}
						}
							
					
						light = (chunkLight - torchLight >= 0.0F) ? chunks[i].light[x][y] - torchLight : 0.0F;
					
						int x1 = x + baseX;
						int y1 = y;							
						int xm = x1 * BLOCK_SIZE; 
						int ym = y1 * BLOCK_SIZE; 
						
						t.setColorRGBA(0, 0, 0, (int)(light * 255));
						t.addVertexWithUV(xm, BLOCK_SIZE + ym, 0, 0, 0.03125);
						t.addVertexWithUV(xm + BLOCK_SIZE, BLOCK_SIZE + ym, 0, 0.0625, 0.03125);
						t.addVertexWithUV(xm + BLOCK_SIZE, ym, 0, 0.0625, 0);
						t.addVertexWithUV(xm, ym, 0, 0, 0);
							
					}
				}
				
				if((int)(baseRenderX) == Chunk.getChunkWidth())
				{
					baseRenderX++;
				}
			}
			t.draw();
	//		}
	//		else
	//		{
	//			t.startDrawingQuads();
	//			for(int i = 0; i < chunks.length; i++) //Loop each X Chunk
	//			{
	//				remainingHeight = height;
	//				int baseX = baseX_F + (i * Chunk.getChunkWidth());
	//				int xoff = 0;
	//				int loopX = 0;
	//				
	//				//Determine the offset to start rendering, and how far to render
	//				//Also ensure rendering stays in bounds. This is for the X rendering calculations
	//				if(i == 0) //First Chunk
	//				{
	//					xoff = (int) (x % Chunk.getChunkWidth());
	//					loopX = (int) (Chunk.getChunkWidth() - (x % Chunk.getChunkWidth()));
	//					remainingWidth -= loopX;
	//					if(remainingWidth < 0) 
	//					{
	//						loopX += remainingWidth;
	//					}
	//				}					
	//				else if(i == chunks.length - 1 && i != 0) //Last Chunk (may never get hit)
	//				{
	//					xoff = 0;
	//					loopX = remainingWidth;
	//					remainingWidth = 0;
	//				}
	//				else //Not tested. Should allow an entire 'middle' chunk to render (this requires something stupid like 1800x3600 resolution to hit)
	//				{
	//					xoff = 0;
	//					loopX = Chunk.getChunkWidth();
	//					remainingWidth -= loopX;
	//				}
	//								
	//				int yoff = 0;
	//				int loopY = 0;
	//				
	//				//Determine the offset to start rendering, and how far to render
	//				//Also ensure rendering stays in bounds. This is for the Y rendering calculations
	//				yoff = (int) (y % world.getHeight());
	//				loopY = (int) (world.getHeight() - (y % world.getHeight()));
	//				remainingHeight -= loopY;
	//				if(remainingHeight < 0) 
	//				{
	//					loopY += remainingHeight;
	//				}
	//				
	//				//Fix random blocks not rendering. Generally occurs on the edge of chunks
	//				if(xoff + loopX < Chunk.getChunkWidth())
	//				{
	//					loopX++;
	//				}
	//				if(yoff + loopY < world.getHeight()) 
	//				{
	//					loopY++;
	//				}
	//				if((int)(y) == world.getHeight()) 
	//				{
	//					y++;
	//				}
	//	
	//				for(int k = xoff; k < xoff + loopX; k++) //x
	//				{	
	//					for(int l = yoff; l < yoff + loopY; l++) //y
	//					{		
	//						int x1 = k + baseX;
	//						int y1 = l;
	//						int xm = x1 * BLOCK_SIZE; 
	//						int ym = y1 * BLOCK_SIZE; 
	//						
	//						t.setColorRGBA(0, 0, 0, (int)(chunks[i].light[k][l] * 255F));
	//						t.addVertexWithUV(xm, BLOCK_SIZE + ym, 0, 0, 0.03125);
	//						t.addVertexWithUV(xm + BLOCK_SIZE, BLOCK_SIZE + ym, 0, 0.0625, 0.03125);
	//						t.addVertexWithUV(xm + BLOCK_SIZE, ym, 0, 0.0625, 0);
	//						t.addVertexWithUV(xm, ym, 0, 0, 0);
	//					}
	//				}
	//				
	//				if((int)(x) == Chunk.getChunkWidth())
	//				{
	//					x++;
	//				}
	//			}
	//			t.draw();
	//		}
						
			GL11.glEnable(GL11.GL_TEXTURE_2D);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
