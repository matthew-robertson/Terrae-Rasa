package net.dimensia.src;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class RenderBlocks extends Render
{
	public void renderBackwall(World world)
	{
		adjustCamera(world);
		final int xsize = (int)(Display.getWidth() / 11.1);
		final int ysize = (int)(Display.getHeight() / 11.1) + 1;
		int x = (int) ((world.player.x - (0.25f * Display.getWidth())) / 6);
		int y = (int) ((world.player.y - (0.25f * Display.getHeight())) / 6);
		if(y < 0) y = 0;
		if(x < 0) x = 0;
		if(xsize + x > world.getWidth()) x = world.getWidth() - xsize;
		if(ysize + y > world.getHeight()) y = world.getHeight() - ysize - 1;
		Chunk[][] chunks = getRequiredChunks(world);
		TERRAIN.bind();
		
		//Settings for lighting
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		//'base' render position (this is a constant offset that needs applied). It is the edge of a chunk, in Blocks, generally the nearest left or up.
		final int baseX_F = (int) ((int)(x / Chunk.getChunkWidth()) * Chunk.getChunkWidth());
		final int baseY_F = (int) ((int)(y / Chunk.getChunkHeight()) * Chunk.getChunkHeight());
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
			
			for(int j = 0; j < chunks[0].length; j++) //Loop all Y chunks
			{
				int baseY = baseY_F + (j * Chunk.getChunkHeight()); 
				int yoff = 0;
				int loopY = 0;
				
				//Determine the offset to start rendering, and how far to render
				//Also ensure rendering stays in bounds. This is for the Y rendering calculations
				if(j == 0) //First Chunk
				{
					yoff = (int) (y % Chunk.getChunkHeight());
					loopY = (int) (Chunk.getChunkHeight() - (y % Chunk.getChunkHeight()));
					remainingHeight -= loopY;
					if(remainingHeight < 0) 
					{
						loopY += remainingHeight;
					}
				}					
				else if(j == chunks[0].length - 1) //Last Chunk (may never get hit)
				{
					yoff = 0;
					loopY = remainingHeight;
					remainingHeight = 0;
				}
				else //Not tested. Should allow an entire 'middle' chunk to render (this requires something stupid like 1800x3600 resolution to hit)
				{
					yoff = 0;
					loopY = Chunk.getChunkHeight();
					remainingHeight -= loopY;
				}
				
				//Fix random blocks not rendering. Generally occurs on the edge of chunks
				if(xoff + loopX < Chunk.getChunkWidth())
				{
					loopX++;
				}
				if(yoff + loopY < Chunk.getChunkHeight()) 
				{
					loopY++;
				}
				if((int)(y) == Chunk.getChunkHeight()) 
				{
					y++;
				}
	
				for(int k = xoff; k < xoff + loopX; k++) //x
				{	
					for(int l = yoff; l < yoff + loopY; l++) //y
					{						
						Block block = chunks[i][j].backWalls[k][l];
						int x1 = k + baseX;
						int y1 = l + baseY;
						
						//Fix lighting bound issues
						int k1 = k;
						int l1 = l;
						while(k1 >= Chunk.getChunkWidth() - 1) 
						{
							k1--;
						}
						while(l1 >= Chunk.getChunkHeight() - 1) 
						{
							l1--;
						}
					
						//Check if the block should be rendered
						if(block.blockID == 0) 
						{
							continue;
						}
						
						if(block.hasMetaData) //large texture
						{
							if(block.metaData == 1) //if the block with metadata is in the top-left corner of the larger block, render it
							{
								int blockHeight = block.blockHeight;
								int blockWidth = block.blockWidth;		
							    float tx = (float)((block.iconIndex / 16) * 16) / 512;
							    float ty = (float)((block.iconIndex % 16) * 16) / 256;
								float tw = tx + (block.textureWidth / 512);
								float th = ty + (block.textureHeight / 256);				
								int xm = x1 * 6; 
								int ym = y1 * 6; 
								
								//the block
								t.setColorRGBA_F(1, 1, 1, 1);
								t.addVertexWithUV(xm, blockHeight + ym, 0, tx, th);
								t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, tw, th);
								t.addVertexWithUV(xm + blockWidth, ym, 0, tw, ty);
								t.addVertexWithUV(xm, ym, 0, tx, ty);	
								
								//Lighting (for the time being)
								t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1][l1 + 1]);
								t.addVertexWithUV(xm, blockHeight + ym, 0, 0, 0.03125);
								t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1 + 1][l1 + 1]);
								t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, 0.0625, 0.03125);
								t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1 + 1][l1]);
								t.addVertexWithUV(xm + blockWidth, ym, 0, 0.0625, 0);
								t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1][l1]);
								t.addVertexWithUV(xm, ym, 0, 0, 0);	
							}
						}
						else //normal texture
						{
							int blockHeight = block.blockHeight;
							int blockWidth = block.blockWidth;		
						    float tx = (float)((block.iconIndex / 16) * 16) / 512;
						    float ty = (float)((block.iconIndex % 16) * 16) / 256;
							float tw = tx + (block.textureWidth / 512);
							float th = ty + (block.textureHeight / 256);				
							int xm = x1 * 6; 
							int ym = y1 * 6; 
							
							//The block
							t.setColorRGBA_F(1, 1, 1, 1);
							t.addVertexWithUV(xm, blockHeight + ym, 0, tx, th);
							t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, tw, th);
							t.addVertexWithUV(xm + blockWidth, ym, 0, tw, ty);
							t.addVertexWithUV(xm, ym, 0, tx, ty);				
							
							//Lighting (for the time being)
							t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1][l1 + 1]);
							t.addVertexWithUV(xm, blockHeight + ym, 0, 0, 0.03125);
							t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1 + 1][l1 + 1]);
							t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, 0.0625, 0.03125);
							t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1 + 1][l1]);
							t.addVertexWithUV(xm + blockWidth, ym, 0, 0.0625, 0);
							t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1][l1]);
							t.addVertexWithUV(xm, ym, 0, 0, 0);				
						}
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
	
	public void render(World world) 
	{
		adjustCamera(world);
		final int xsize = (int)(Display.getWidth() / 11.1);
		final int ysize = (int)(Display.getHeight() / 11.1) + 1;
		int x = (int) ((world.player.x - (0.25f * Display.getWidth())) / 6);
		int y = (int) ((world.player.y - (0.25f * Display.getHeight())) / 6);
		if(y < 0) y = 0;
		if(x < 0) x = 0;
		if(xsize + x > world.getWidth()) x = world.getWidth() - xsize;
		if(ysize + y > world.getHeight()) y = world.getHeight() - ysize - 1;
		Chunk[][] chunks = getRequiredChunks(world);
		TERRAIN.bind();
		
		//Settings for lighting
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		//'base' render position (this is a constant offset that needs applied). It is the edge of a chunk, in Blocks, generally the nearest left or up.
		final int baseX_F = (int) ((int)(x / Chunk.getChunkWidth()) * Chunk.getChunkWidth());
		final int baseY_F = (int) ((int)(y / Chunk.getChunkHeight()) * Chunk.getChunkHeight());
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
			
			for(int j = 0; j < chunks[0].length; j++) //Loop all Y chunks
			{
				int baseY = baseY_F + (j * Chunk.getChunkHeight()); 
				int yoff = 0;
				int loopY = 0;
				
				//Determine the offset to start rendering, and how far to render
				//Also ensure rendering stays in bounds. This is for the Y rendering calculations
				if(j == 0) //First Chunk
				{
					yoff = (int) (y % Chunk.getChunkHeight());
					loopY = (int) (Chunk.getChunkHeight() - (y % Chunk.getChunkHeight()));
					remainingHeight -= loopY;
					if(remainingHeight < 0) 
					{
						loopY += remainingHeight;
					}
				}					
				else if(j == chunks[0].length - 1) //Last Chunk (may never get hit)
				{
					yoff = 0;
					loopY = remainingHeight;
					remainingHeight = 0;
				}
				else //Not tested. Should allow an entire 'middle' chunk to render (this requires something stupid like 1800x3600 resolution to hit)
				{
					yoff = 0;
					loopY = Chunk.getChunkHeight();
					remainingHeight -= loopY;
				}
				
				//Fix random blocks not rendering. Generally occurs on the edge of chunks
				if(xoff + loopX < Chunk.getChunkWidth())
				{
					loopX++;
				}
				if(yoff + loopY < Chunk.getChunkHeight()) 
				{
					loopY++;
				}
				if((int)(y) == Chunk.getChunkHeight()) 
				{
					y++;
				}
	
				for(int k = xoff; k < xoff + loopX; k++) //x
				{	
					for(int l = yoff; l < yoff + loopY; l++) //y
					{						
						Block block = chunks[i][j].blocks[k][l];
						int x1 = k + baseX;
						int y1 = l + baseY;
						
						//Fix lighting bound issues
						int k1 = k;
						int l1 = l;
						while(k1 >= Chunk.getChunkWidth() - 1) 
						{
							k1--;
						}
						while(l1 >= Chunk.getChunkHeight() - 1) 
						{
							l1--;
						}
					
						//Check if the block should be rendered
						if(block.blockID == 0) 
						{
							continue;
						}
						if (block.getTileMap() != ' '){
							TERRAIN_GROUND.bind();
						}
						if(block.hasMetaData) //large texture
						{
							if(block.metaData == 1) //if the block with metadata is in the top-left corner of the larger block, render it
							{
								int blockHeight = block.blockHeight;
								int blockWidth = block.blockWidth;		
							    float tx = (float)((block.iconIndex % 16) * 16) / 256;
							    float ty = (float)((block.iconIndex / 16) * 16) / 512;
								float tw = tx + (block.textureWidth / 256);
								float th = ty + (block.textureHeight / 512);				
								int xm = x1 * 6; 
								int ym = y1 * 6; 
								
								//the block
								t.setColorRGBA_F(1, 1, 1, 1);
								t.addVertexWithUV(xm, blockHeight + ym, 0, tx, th);
								t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, tw, th);
								t.addVertexWithUV(xm + blockWidth, ym, 0, tw, ty);
								t.addVertexWithUV(xm, ym, 0, tx, ty);	
								
								//Lighting (for the time being)
								/*t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1][l1 + 1]);
								t.addVertexWithUV(xm, blockHeight + ym, 0, 0, 0.03125);
								t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1 + 1][l1 + 1]);
								t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, 0.0625, 0.03125);
								t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1 + 1][l1]);
								t.addVertexWithUV(xm + blockWidth, ym, 0, 0.0625, 0);
								t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1][l1]);
								t.addVertexWithUV(xm, ym, 0, 0, 0);*/
							}
						}
						else //normal texture
						{
							int blockHeight = block.blockHeight;
							int blockWidth = block.blockWidth;		
						    float tx = (float)((block.iconIndex % 16) * 16) / 256;
						    float ty = (float)((block.iconIndex / 16) * 16) / 512;
							float tw = tx + (block.textureWidth / 256);
							float th = ty + (block.textureHeight / 512);				
							int xm = x1 * 6; 
							int ym = y1 * 6; 
							
							//The block
							t.setColorRGBA_F(1, 1, 1, 1);
							t.addVertexWithUV(xm, blockHeight + ym, 0, tx, th);
							t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, tw, th);
							t.addVertexWithUV(xm + blockWidth, ym, 0, tw, ty);
							t.addVertexWithUV(xm, ym, 0, tx, ty);				
							
							//Lighting (for the time being)
							/*t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1][l1 + 1]);
							t.addVertexWithUV(xm, blockHeight + ym, 0, 0, 0.03125);
							t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1 + 1][l1 + 1]);
							t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, 0.0625, 0.03125);
							t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1 + 1][l1]);
							t.addVertexWithUV(xm + blockWidth, ym, 0, 0.0625, 0);
							t.setColorRGBA_F(0, 0, 0, chunks[i][j].light[k1][l1]);
							t.addVertexWithUV(xm, ym, 0, 0, 0);*/				
						}
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

	/**
	 * @deprecated doesnt do anything right now
	 * 
	 * @param world
	 * @param chunks
	 * @param x
	 * @param width
	 * @param y
	 * @param height
	 */
	private void renderLighting(World world, Chunk[][] chunks, int x, int width, int y, int height)
	{
		int baseX_F = (int) ((int)(x / Chunk.getChunkWidth()) * Chunk.getChunkWidth());
		int baseY_F = (int) ((int)(y / Chunk.getChunkHeight()) * Chunk.getChunkHeight());
		int remainingWidth = width;
		int remainingHeight = height;
		boolean isEmitingLight = false;
	
		if(world.player.inventory.getMainInventoryStack(world.player.selectedSlot) != null)
		{
			if((world.player.inventory.getMainInventoryStack(world.player.selectedSlot).getItemID() == Block.torch.blockID))
			{
				isEmitingLight = true;
			}	
		}
		/*		
		if(world.player.inventory.getMainInventory()[world.player.selectedSlot] == null || (world.player.inventory.getMainInventory()[world.player.selectedSlot].getItemID() == Block.torch.blockID))
		{
			return;
		}
		*/
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		//int xm = 250;
		//int ym = 1500; 		
		int strength = 15;
		float[][] lightMap = LightingEngine.generateLightSource(world, (int)(world.player.x / 6), (int)(world.player.y / 6), strength, 1.0f);
		int playerx = (int) (world.player.x / 6);
		int playery = (int) (world.player.y / 6);
				
		t.startDrawingQuads();
		try
		{
			for(int i = 0; i < chunks.length; i++)
			{
				remainingHeight = height;
				int baseX = baseX_F + (i * Chunk.getChunkWidth());
				int xoff = 0;
				int loopX = 0;
				
				if(i == 0)
				{
					xoff = (int) (x % Chunk.getChunkWidth());
					loopX = (int) (Chunk.getChunkWidth() - (x % Chunk.getChunkWidth()));
					remainingWidth -= loopX;
					if(remainingWidth < 0) 
					{
						loopX += remainingWidth;
					}
				}					
				else if(i == chunks.length - 1 && i != 0)
				{
					xoff = 0;
					loopX = remainingWidth;
					remainingWidth = 0;
				}
				else
				{
					xoff = 0;
					loopX = Chunk.getChunkWidth();
					remainingWidth -= loopX;
				}
				
				for(int j = 0; j < chunks[0].length; j++)
				{
					int baseY = baseY_F + (j * Chunk.getChunkHeight()); 
					int yoff = 0;
					int loopY = 0;
					
					if(j == 0)
					{
						yoff = (int) (y % Chunk.getChunkHeight());
						loopY = (int) (Chunk.getChunkHeight() - (y % Chunk.getChunkHeight()));
						remainingHeight -= loopY;
						if(remainingHeight < 0) 
						{
							loopY += remainingHeight;
						}
					}					
					else if(j == chunks[0].length - 1)
					{
						yoff = 0;
						loopY = remainingHeight;
						remainingHeight = 0;
					}
					else
					{
						yoff = 0;
						loopY = Chunk.getChunkHeight();
						remainingHeight -= loopY;
					}
					
		//System.out.printf(">> %5s %5s %5s %5s %5s %5s %5s %5s <<\n", baseX, baseY, xoff, loopX, yoff, loopY, remainingWidth, remainingHeight);
					
					if(xoff + loopX < Chunk.getChunkWidth())
					{
						loopX++;
					}
					if(yoff + loopY < Chunk.getChunkHeight())
					{
						loopY++;
					}
					
					if((int)(y) == Chunk.getChunkHeight())
					{
						y++;
					}
		
					for(int k = xoff; k < xoff + loopX; k++) //x
					{	
						for(int l = yoff; l < yoff + loopY; l++) //y
						{
							if(isEmitingLight && k > MathHelper.zeroOrGreater(playerx - strength - 1) && k < playerx + strength && l > playery - strength - 1 && l < playery + strength)
							{
								continue;
							}
							
							int xm = (baseX + k) * 6;
							int ym = (baseY + l) * 6;
							
							int k1 = k + baseX;
							int l1 = l + baseY;
							
							t.setColorRGBA_F(0, 0, 0, world.getLight(k1, l1 + 1));
							t.addVertexWithUV(xm , ym + 6, 0, 0, 1);
							t.setColorRGBA_F(0, 0, 0, world.getLight(k1 + 1, l1 + 1));
							t.addVertexWithUV(xm + 6, ym + 6, 0, 1, 1);
							t.setColorRGBA_F(0, 0, 0, world.getLight(k1 + 1, l1));
							t.addVertexWithUV(xm + 6, ym, 0, 1, 0);
							t.setColorRGBA_F(0, 0, 0, world.getLight(k1, l1));
							t.addVertexWithUV(xm, ym, 0, 0, 0);			
						}
					}
				}
				if((int)(x) == Chunk.getChunkWidth())
				{
					x++;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(isEmitingLight) //Torch, handheld
		{
			for(int i = 0; i < strength * 2; i++) //x
			{	
				for(int k = 0; k < strength * 2; k++) //y
				{
					int xm = (int) MathHelper.zeroOrGreater(MathHelper.multipleOfSix((int) ((i - strength) * 6 + (world.player.x / 6) * 6)));
					int ym = (int) MathHelper.zeroOrGreater(MathHelper.multipleOfSix((int) ((k - strength) * 6 + (world.player.y / 6) * 6)));
					
					t.setColorRGBA_F(0, 0, 0, lightMap[i][k + 1]);
					t.addVertexWithUV(xm , ym + 6, 0, 0, 1);
					t.setColorRGBA_F(0, 0, 0, lightMap[i + 1][k + 1]);
					t.addVertexWithUV(xm + 6, ym + 6, 0, 1, 1);
					t.setColorRGBA_F(0, 0, 0, lightMap[i + 1][k]);
					t.addVertexWithUV(xm + 6, ym, 0, 1, 0);
					t.setColorRGBA_F(0, 0, 0, lightMap[i][k]);
					t.addVertexWithUV(xm, ym, 0, 0, 0);
				}
			}			
			
			//TODO: Bug where the torch doesnt render the bottom and rightmost columns at all.
		}
		
		t.draw();		
		GL11.glEnable(GL11.GL_TEXTURE_2D);		
	}
	
	private Chunk[][] getRequiredChunks(World world)
	{
		Chunk[][] chunks;
		final int width = (int)((float)(Display.getWidth() / 6) / 1.85);;
		final int height = (int)((float)(Display.getHeight() / 6) / 1.85);;
		float x = ((world.player.x - (0.25f * Display.getWidth())) / 6);
		float y = ((world.player.y - (0.25f * Display.getHeight())) / 6);
		if(y < 0) y = 0; 
		if(x < 0) x = 0;
		if(width + x > world.getWidth()) x = world.getWidth() - width - 1;
		if(height + y > world.getHeight()) y = world.getHeight() - height - 1;
			
		int xBegin = (int) (x / Chunk.getChunkWidth());
		int yBegin = (int) (y / Chunk.getChunkHeight()); 
		int xEnd = (int) ((x + width) / Chunk.getChunkWidth());
		int yEnd = (int) ((y + height) / Chunk.getChunkHeight());
		chunks = new Chunk[(xEnd - xBegin > 0) ? xEnd - xBegin + 1 : 1][(yEnd - yBegin > 0) ? yEnd - yBegin + 1 : 1];
		
		xEnd++;
		yEnd++;
		
		int k = 0;
		int l = 0;
				
		for(int i = xBegin; i < xEnd; i++)
		{
			l = 0;
			for(int j = yBegin; j < yEnd; j++)
			{
				chunks[k][l] = world.getChunk(i, j);
				l++;
			}
			k++;
		}
		
		return chunks;
	}
	
	private void renderBlockEntity(int x, int y, Block block)
	{			
		if(block.blockID == 0) //nothing to render
		{
			return;
		}
		
		if(block.hasMetaData) //large texture
		{
			if(block.metaData == 1) //if the block with metadata is in the top-left corner of the larger block, render it
			{
				int blockHeight = block.blockHeight;
				int blockWidth = block.blockWidth;		
			    float tx = (float)((block.iconIndex / 16) * 16) / 512;
			    float ty = (float)((block.iconIndex % 16) * 16) / 256;
				float tw = tx + (block.textureWidth / 512);
				float th = ty + (block.textureHeight / 256);				
				int xm = x * 6; 
				int ym = y * 6; 
				
				t.addVertexWithUV(xm, blockHeight + ym, 0, tx, th);
				t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, tw, th);
				t.addVertexWithUV(xm + blockWidth, ym, 0, tw, ty);
				t.addVertexWithUV(xm, ym, 0, tx, ty);	
			}
		}
		else //normal texture
		{
			int blockHeight = block.blockHeight;
			int blockWidth = block.blockWidth;		
		    float tx = (float)((block.iconIndex / 16) * 16) / 512;
		    float ty = (float)((block.iconIndex % 16) * 16) / 256;
			float tw = tx + (block.textureWidth / 512);
			float th = ty + (block.textureHeight / 256);				
			int xm = x * 6; 
			int ym = y * 6; 
			
			t.addVertexWithUV(xm, blockHeight + ym, 0, tx, th);
			t.addVertexWithUV(xm + blockWidth, blockHeight + ym, 0, tw, th);
			t.addVertexWithUV(xm + blockWidth, ym, 0, tw, ty);
			t.addVertexWithUV(xm, ym, 0, tx, ty);				
		}
	}
}
