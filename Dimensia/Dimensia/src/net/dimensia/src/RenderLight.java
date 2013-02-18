package net.dimensia.src;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class RenderLight extends Render
{	
	public void render(World world, EntityLivingPlayer player)
	{
		adjustCamera(world, player);
		final int xsize = (int)(Display.getWidth() / 11.1);
		final int ysize = (int)(Display.getHeight() / 11.1) + 1;
		int x = (int) ((player.x - (0.25f * Display.getWidth())) / 6);
		int y = (int) ((player.y - (0.25f * Display.getHeight())) / 6);
		if(y < 0) y = 0;
		if(x < 0) x = 0;
		if(xsize + x > world.getWidth()) x = world.getWidth() - xsize;
		if(ysize + y > world.getHeight()) y = world.getHeight() - ysize - 1;
		Chunk[] chunks = ChunkUtils.getRequiredChunks(world, player);
		TERRAIN.bind();
		
		//Settings for lighting
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		//'base' render position (this is a constant offset that needs applied). It is the edge of a chunk, in Blocks, generally the nearest left or up.
		final int baseX_F = (int) ((int)(x / Chunk.getChunkWidth()) * Chunk.getChunkWidth());
		//final int baseY_F = (int) ((int)(y));
		//set the amount of remaining blocks, initially (this is reset later)
		int remainingWidth = xsize;
		int remainingHeight = ysize;
		
		t.startDrawingQuads();
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
			
			int baseY = 0;//baseY_F + (j * world.getHeight()); 
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
					int y1 = l + baseY;
					
					//Fix lighting bound issues
					int k1 = k;
					int l1 = l;
					while(k1 >= Chunk.getChunkWidth() - 1) 
					{
						k1--;
					}
					while(l1 >= world.getHeight() - 1) 
					{
						l1--;
					}
				
					
					int blockHeight = 6;
					int blockWidth = 6;		
					int xm = x1 * 6; 
					int ym = y1 * 6; 
					
					//Lighting (for the time being)
					t.setColorRGBA_F(0, 0, 0, chunks[i].light[k1][l1]);
					t.addVertexWithUV(xm, blockHeight + ym, 0, 0, 0.03125);
					t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, 0.0625, 0.03125);
					t.addVertexWithUV(xm + blockWidth, ym, 0, 0.0625, 0);
					t.addVertexWithUV(xm, ym, 0, 0, 0);
				}
			}
			
			if((int)(x) == Chunk.getChunkWidth())
			{
				x++;
			}
		}
		t.draw();
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
	}
}
