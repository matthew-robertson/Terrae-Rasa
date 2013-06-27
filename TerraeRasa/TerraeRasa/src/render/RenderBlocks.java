package render;

import io.Chunk;

import org.lwjgl.opengl.Display;

import utils.ChunkUtils;
import world.World;
import blocks.Block;
import entities.EntityPlayer;

public class RenderBlocks extends Render
{
	public void renderBackwall(World world, EntityPlayer player)
	{
		adjustCameraToLastPosition();
		final int xsize = (int)(Display.getWidth() / 11.1);
		final int ysize = (int)(Display.getHeight() / 11.1) + 6;
		int x = (int) ((player.x - (0.25f * Display.getWidth())) / 6);
		int y = (int) ((player.y - (0.25f * Display.getHeight())) / 6);
		if(y < 0) y = 0;
		if(x < 0) x = 0;
		if(xsize + x > world.getWidth()) x = world.getWidth() - xsize;
		if(ysize + y > world.getHeight()) y = world.getHeight() - ysize - 1;
		Chunk[] chunks = ChunkUtils.getRequiredChunks(world, player);
		TERRAIN_GROUND.bind();
		
		//'base' render position (this is a constant offset that needs applied). It is the edge of a chunk, in Blocks,
		//generally the nearest left or up.
		final int baseX_F = (int) ((int)(x / Chunk.getChunkWidth()) * Chunk.getChunkWidth());
		//set the amount of remaining blocks, initially (this is reset later)
		int remainingWidth = xsize;
		int remainingHeight = ysize;
		
		t.startDrawingQuads();
		t.setColorRGBA_F(1, 1, 1, 1);
		for(int i = 0; i < chunks.length; i++) //Loop each X Chunk
		{
			remainingHeight = ysize;
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

			final int blockHeight = 6;
			final int blockWidth = 6;
			for(int k = xoff; k < xoff + loopX; k++) //x
			{	
				for(int l = yoff; l < yoff + loopY; l++) //y
				{						
					Block block = chunks[i].backWalls[k][l];
					int x1 = k + baseX;
					int y1 = l;
					//Check if the block should be rendered
					if(block.id == Block.backAir.id) 
					{
						continue;
					}
				    double tx = block.iconX / TEXTURES_PER_ROW;
				    double ty = block.iconY / TEXTURES_PER_COLUMN;
				    double tw = tx + ((double)block.textureWidth / (double)TEXTURE_SHEET_WIDTH);
				    double th = ty + ((double)block.textureHeight / (double)TEXTURE_SHEET_HEIGHT);				
					int xm = x1 * BLOCK_SIZE; 
					int ym = y1 * BLOCK_SIZE; 
																	
					t.addVertexWithUV(xm, blockHeight + ym, 0, tx, th);
					t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, tw, th);
					t.addVertexWithUV(xm + blockWidth, ym, 0, tw, ty);
					t.addVertexWithUV(xm, ym, 0, tx, ty);				
					
				}
			}
			
			if((int)(x) == Chunk.getChunkWidth())
			{
				x++;
			}
		}
		t.draw();
	}
	
	public void render(World world, EntityPlayer player) 
	{
		adjustCameraToLastPosition();
		final int xsize = (int)(Display.getWidth() / 11.1);
		final int ysize = (int)(Display.getHeight() / 11.1) + 6;
		int x = (int) ((player.x - (0.25f * Display.getWidth())) / 6);
		int y = (int) ((player.y - (0.25f * Display.getHeight())) / 6);
		if(y < 0) y = 0;
		if(x < 0) x = 0;
		if(xsize + x > world.getWidth()) x = world.getWidth() - xsize;
		if(ysize + y > world.getHeight()) y = world.getHeight() - ysize - 1;
		Chunk[] chunks = ChunkUtils.getRequiredChunks(world, player);
		TERRAIN_GROUND.bind();
		
		//'base' render position (this is a constant offset that needs applied). It is the edge of a chunk, in Blocks,
		//generally the nearest left or up.
		final int baseX_F = (int) ((int)(x / Chunk.getChunkWidth()) * Chunk.getChunkWidth());
		//set the amount of remaining blocks, initially (this is reset later)
		int remainingWidth = xsize;
		int remainingHeight = ysize;
		
		t.startDrawingQuads();
		t.setColorRGBA_F(1, 1, 1, 1);
		for(int i = 0; i < chunks.length; i++) //Loop each X Chunk
		{
			remainingHeight = ysize;
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
					Block block = chunks[i].blocks[k][l];
					int x1 = k + baseX;
					int y1 = l;
										
					//Check if the block should be rendered
					if(block.id == 0) 
					{
						continue;
					}
					if(block.hasMetaData) //large texture
					{
						if(block.metaData == 1) //if the block with metadata is in the top-left corner of the larger block, render it
						{
							double blockHeight = block.blockHeight;
							double blockWidth = block.blockWidth;		
							double tx = block.iconX / TEXTURES_PER_ROW;
						    double ty = block.iconY / TEXTURES_PER_COLUMN;
						    double tw = tx + (block.textureWidth / TEXTURE_SHEET_WIDTH);
						    double th = ty + (block.textureHeight / TEXTURE_SHEET_HEIGHT);			
							int xm = x1 * 6; 
							int ym = y1 * 6; 
							
							//the block
							t.addVertexWithUV(xm, blockHeight + ym, 0, tx, th);
							t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, tw, th);
							t.addVertexWithUV(xm + blockWidth, ym, 0, tw, ty);
							t.addVertexWithUV(xm, ym, 0, tx, ty);	
							
						}
					}
					else //normal texture
					{
						int blockHeight = 6;
						int blockWidth = 6;
					    double tx = block.iconX / TEXTURES_PER_ROW;
					    double ty = block.iconY / TEXTURES_PER_COLUMN;
					    double tw = tx + (block.textureWidth / TEXTURE_SHEET_WIDTH);
					    double th = ty + (block.textureHeight / TEXTURE_SHEET_HEIGHT);				
						int xm = x1 * BLOCK_SIZE; 
						int ym = y1 * BLOCK_SIZE; 
																	
						//The block
						t.addVertexWithUV(xm, blockHeight + ym, 0, tx, th);
						t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, tw, th);
						t.addVertexWithUV(xm + blockWidth, ym, 0, tw, ty);
						t.addVertexWithUV(xm, ym, 0, tx, ty);											
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
}
