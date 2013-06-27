package utils;

import io.Chunk;

import java.util.Enumeration;

import world.World;

import enums.EnumEventType;


import blocks.BlockLight;

public class LightUtils 
{
	private final double LIGHT_BLOCK_DISSIPATES = 0.075;
	private final int DIFFUSE_LIGHT_CHECK_RADIUS = 20;

	public void applyLightSource(World world, int xSource, int ySource, final int radius, final double strength)
	{
		//not bounds safe
		
		for(int x = xSource - radius; x < xSource + radius; x++)
		{
			for(int y = ySource - radius; y < ySource + radius; y++)
			{
				Vector2 vect = subtractVector(x, y, xSource, ySource);				
				double len = vect.length();			
				
				if (x + 1 > world.getWidth() - 1 || x - 1 < 0 || y + 1 > world.getHeight() - 1 || y - 1 < 0)  
                    continue;  
                
				
				if(len <= radius)
				{
					int x1 = xSource;
					int y1 = ySource;
					int x2 = x;
					int y2 = y;
					
					int dx = Math.abs(x2 - x1);
					int dy = Math.abs(y2 - y1);

					int sx = (x1 < x2) ? 1 : -1;
					int sy = (y1 < y2) ? 1 : -1;

					int err = dx - dy;
					int blockCount = 0;

					while (true) {
					   	
						//Count blocks in the way
					   	//if(solid[x1][y1] == 1)
					   	//	blockCount++;
					   	
						//THIS IS LIKELY AN ERROR!
						if(world.getBlock(x1, y1).getIsSolid())
							blockCount++;
							
					    if (x1 == x2 && y1 == y2) {
					        break;
					    }

					    int e2 = 2 * err;

					    if (e2 > -dy) {
					        err = err - dy;
					        x1 = x1 + sx;
					    }

					    if (e2 < dx) {
					        err = err + dx;
					        y1 = y1 + sy;
					    }		   
					}
					
					double maxStrength = strength - (double) (len / radius * strength);
					double light = sat((maxStrength - (LIGHT_BLOCK_DISSIPATES * blockCount)));
			
					//if(light > world.getDiffuseLight(x,y))
					//{
						world.setDiffuseLight(x,y,world.getDiffuseLight(x, y) + light);
					//}
				}				
			}		
		}		
	
		Enumeration<String> keys = world.getChunks().keys();
        while (keys.hasMoreElements()) 
        {
            Object key = keys.nextElement();
            String str = (String) key;
            Chunk chunk = world.getChunks().get(str);
            chunk.updateChunkLight();
        }
    
	}
	
	public double[][] getLightMap(World world, int xSource, int ySource, final int radius, final double strength)
	{
		//not bounds safe
		double[][] lightMap = new double[radius * 2][radius * 2];
		
		for(int x = xSource - radius; x < xSource + radius; x++)
		{
			for(int y = ySource - radius; y < ySource + radius; y++)
			{
				Vector2 vect = subtractVector(x, y, xSource, ySource);				
				double len = vect.length();			
				
				if (x + 1 > world.getWidth() - 1 || x - 1 < 0 || y + 1 > world.getHeight() - 1 || y - 1 < 0)  
                    continue;  
                
				if(len <= radius)
				{
					int x1 = xSource;
					int y1 = ySource;
					int x2 = x;
					int y2 = y;
					
					int dx = Math.abs(x2 - x1);
					int dy = Math.abs(y2 - y1);

					int sx = (x1 < x2) ? 1 : -1;
					int sy = (y1 < y2) ? 1 : -1;

					int err = dx - dy;
					int blockCount = 0;

					while (true) 
					{
						//Count blocks in the way
					   	//if(solid[x1][y1] == 1)
					   	//	blockCount++;
					   	
						//THIS IS LIKELY AN ERROR!
						if(world.getBlock(x1, y1).getIsSolid())
							blockCount++;
							
					    if (x1 == x2 && y1 == y2) {
					        break;
					    }

					    int e2 = 2 * err;

					    if (e2 > -dy) {
					        err = err - dy;
					        x1 = x1 + sx;
					    }

					    if (e2 < dx) {
					        err = err + dx;
					        y1 = y1 + sy;
					    }		   
					}
					
					double maxStrength = strength - (double) (len / radius * strength);
					double light = sat((maxStrength - (LIGHT_BLOCK_DISSIPATES * blockCount)));
			
					lightMap[x - (xSource - radius)][y - (ySource - radius)] = light;
					//if(light > world.getDiffuseLight(x,y))
					//{
					//	world.setDiffuseLight(x,y,world.getDiffuseLight(x, y) + light);
					//}
				}				
			}		
		}		
	/*
		System.out.println();
		for(int i = 0; i < 20; i++)
		{
			for(int j = 0; j < 20; j++)
			{
				System.out.printf("%.2f ",lightMap[i][j]);
			}
			System.out.println();
		}
		*/
		return lightMap;
    }
	
	public void removeLightSource(World world, int xSource, int ySource, final int radius, final double strength)
	{
		//not bounds safe
		
		for(int x = xSource - radius; x < xSource + radius; x++)
		{
			for(int y = ySource - radius; y < ySource + radius; y++)
			{
				Vector2 vect = subtractVector(x, y, xSource, ySource);				
				double len = vect.length();			
				
				if (x + 1 > world.getWidth() - 1 || x - 1 < 0 || y + 1 > world.getHeight() - 1 || y - 1 < 0)  
                    continue;  
                
				
				if(len <= radius)
				{
					int x1 = xSource;
					int y1 = ySource;
					int x2 = x;
					int y2 = y;
					
					int dx = Math.abs(x2 - x1);
					int dy = Math.abs(y2 - y1);

					int sx = (x1 < x2) ? 1 : -1;
					int sy = (y1 < y2) ? 1 : -1;

					int err = dx - dy;
					int blockCount = 0;

					while (true) {
					   	
						//Count blocks in the way
					   	//if(solid[x1][y1] == 1)
					   	//	blockCount++;
					   	
						//THIS IS LIKELY AN ERROR!
						if(world.getBlock(x1, y1).getIsSolid())
							blockCount++;
							
					    if (x1 == x2 && y1 == y2) {
					        break;
					    }

					    int e2 = 2 * err;

					    if (e2 > -dy) {
					        err = err - dy;
					        x1 = x1 + sx;
					    }

					    if (e2 < dx) {
					        err = err + dx;
					        y1 = y1 + sy;
					    }		   
					}
					
					double maxStrength = strength - (double) (len / radius * strength);
					double light = sat((maxStrength - (LIGHT_BLOCK_DISSIPATES * blockCount)));
			
					//if(light > world.getDiffuseLight(x,y))
					//{
						world.setDiffuseLight(x,y, world.getDiffuseLight(x, y) - light);
					//}
				}				
			}		
		}		
	
		Enumeration<String> keys = world.getChunks().keys();
        while (keys.hasMoreElements()) 
        {
            Object key = keys.nextElement();
            String str = (String) key;
            Chunk chunk = world.getChunks().get(str);
            chunk.updateChunkLight();
        }
    
	}
	
	public void applyAmbient(World world)
	{		
		double lightStrength = world.getLightLevel();
		
		int AVG_HEIGHT = world.getHeight() - 1;
		
		if(AVG_HEIGHT >= world.getHeight())
			AVG_HEIGHT = world.getHeight() - 1;
		//bounds are band-aid'd
		
		for(int x = 1; x < world.getWidth(); x++)
		{
			if (x + 1 > world.getWidth() - 1 || x - 1 < 0) //|| y + 1 > world.GetLength(1) - 1 || y - 1 < 0)  
				continue;  
            
			int y = 1;
			double tmpLight = lightStrength;
			int leftOffset = 1;
			int rightOffset = 1;
			
			while(y < AVG_HEIGHT && tmpLight > 0.0F)
			{
				if(world.getBlock(x, y).getIsSolid())
				{
					tmpLight -= LIGHT_BLOCK_DISSIPATES;
				}
				
				for(leftOffset = 1; leftOffset < 5; leftOffset++)
				{
					if(x - leftOffset < 0) 
						continue;
					
					if(world.getBlock(x - leftOffset, y).getIsSolid() /*&& solid[x - leftOffset][y + 1] == 0*/)
					{
						double leftLight = tmpLight - ((leftOffset - 1) * LIGHT_BLOCK_DISSIPATES);
						for(int j = y; j < AVG_HEIGHT; j++)
						{	
							if(world.getBlock(x - leftOffset, j).getIsSolid())
								leftLight -= LIGHT_BLOCK_DISSIPATES;
													
							if(world.getAmbientLight(x - leftOffset, j) < leftLight)
								world.setAmbientLight(x - leftOffset, j, leftLight);
							
							if(leftLight <= 0.0F)
							{
								break;
							}
						}
					}
				}
			
				for(rightOffset = 1; rightOffset < 5; rightOffset++)
				{
					if(x + rightOffset >= world.getWidth())
						continue;
					if(world.getBlock(x + rightOffset, y).getIsSolid() /* && solid[x + rightOffset][y + 1] == 0 */)
					{
						double rightLight = tmpLight - ((rightOffset - 1) * LIGHT_BLOCK_DISSIPATES);;
						for(int j = y; j < AVG_HEIGHT; j++)
						{	
							if(world.getBlock(x + rightOffset, j).getIsSolid())
								rightLight -= LIGHT_BLOCK_DISSIPATES;
													
							if(world.getAmbientLight(x + rightOffset, j) < rightLight)
								world.setAmbientLight(x + rightOffset, j, rightLight);
							
							if(rightLight <= 0.0F)
							{
								break;
							}
						}
						if(rightLight <= 0.0F || x + rightOffset >= world.getWidth() - 1)
						{
							break;
						}
					}
				}
				//if(solid[x+1][y] == 1)
				//{
					
				//}
				
				if(world.getAmbientLight(x, y) < tmpLight)
					world.setAmbientLight(x, y, tmpLight);
				
				y++;
			}
			
			//System.out.println("x="+ x);
		}
		
		Enumeration<String> keys = world.getChunks().keys();
        while (keys.hasMoreElements()) 
        {
            Object key = keys.nextElement();
            String str = (String) key;
            Chunk chunk = world.getChunks().get(str);
            chunk.updateChunkLight();
        }
       
    }	
	
	public void applyAmbientChunk(World world, Chunk chunk)
	{
		chunk.clearAmbientLight();
		double lightStrength = world.getLightLevel();
		
		int AVG_HEIGHT = world.getHeight() - 1;
		
		//if(AVG_HEIGHT >= world.getHeight())
		//	AVG_HEIGHT = world.getHeight() - 1;
		///bounds are band-aid'd
		
		for(int x = 0; x < Chunk.getChunkWidth(); x++)
		{
			int y = 1;
			double tmpLight = lightStrength;
			int leftOffset = 1;
			int rightOffset = 1;
			
			while(y < AVG_HEIGHT && tmpLight > 0.0F)
			{
				if(chunk.blocks[x][y].getIsSolid())
				{
					tmpLight -= LIGHT_BLOCK_DISSIPATES;
				}
				
				for(leftOffset = 1; leftOffset < 5; leftOffset++)
				{
					if(x - leftOffset < 0) 
						continue;
					
					if(chunk.blocks[x - leftOffset][y].getIsSolid() /*&& solid[x - leftOffset][y + 1] == 0*/)
					{
						double leftLight = tmpLight - ((leftOffset - 1) * LIGHT_BLOCK_DISSIPATES);
						for(int j = y; j < AVG_HEIGHT; j++)
						{	
							if(chunk.blocks[x - leftOffset][j].getIsSolid())
								leftLight -= LIGHT_BLOCK_DISSIPATES;
													
							if(chunk.ambientLight[x - leftOffset][j] < leftLight)
								chunk.ambientLight[x - leftOffset][j] =  leftLight;
							
							if(leftLight <= 0.0F)
							{
								break;
							}
						}
					}
				}
			
				for(rightOffset = 1; rightOffset < 5; rightOffset++)
				{
					if(x + rightOffset >= Chunk.getChunkWidth())
						continue;
					
					if(chunk.blocks[x + rightOffset][y].getIsSolid() /* && solid[x + rightOffset][y + 1] == 0 */)
					{
						double rightLight = tmpLight - ((rightOffset - 1) * LIGHT_BLOCK_DISSIPATES);
						for(int j = y; j < AVG_HEIGHT; j++)
						{	
							if(chunk.blocks[x + rightOffset][j].getIsSolid())
								rightLight -= LIGHT_BLOCK_DISSIPATES;
													
							if(chunk.ambientLight[x + rightOffset][j] < rightLight)
								chunk.ambientLight[x + rightOffset][j] = rightLight;
							
							if(rightLight <= 0.0F)
							{
								break;
							}
						}
						if(rightLight <= 0.0F || x + rightOffset >= Chunk.getChunkWidth() - 1)
						{
							break;
						}
					}
				}
				//if(solid[x+1][y] == 1)
				//{
					
				//}
				
				if(chunk.ambientLight[x][y] < tmpLight)
					chunk.ambientLight[x][y] = tmpLight;
				
				y++;
			}
			
			//System.out.println("x="+ x);
		}
		
	    chunk.setLightUpdated(false);
       
    }	

	public void blockUpdateAmbient(World world, int x, int y, EnumEventType eventType)
	{
		if(eventType == EnumEventType.EVENT_BLOCK_BREAK)
		{
			ambientUpdateBlockBreak(world, x, y);
		//	fixDiffuseLight(world, x, y);
		}
		else if(eventType == EnumEventType.EVENT_BLOCK_PLACE)
		{   
			applyAmbientChunk(world, world.getChunk_Division(x));
		//	fixDiffuseLight(world, x, y);
			
			/*
			if(y == 0) 
			{
				double lightStrength = world.getLightLevel() - LIGHT_BLOCK_DISSIPATES;
				
				int AVG_HEIGHT = world.getHeight() - 1;
				
				if(AVG_HEIGHT >= world.getHeight())
					AVG_HEIGHT = world.getHeight() - 1;
				//bounds are band-aid'd
				
				int yChunk = 1;
				double tmpLight = lightStrength;
				int leftOffset = 1;
				int rightOffset = 1;
				
				while(yChunk < AVG_HEIGHT && tmpLight > 0.0F)
				{
					if(!world.getBlock(x, yChunk).isPassable())
					{
						tmpLight -= LIGHT_BLOCK_DISSIPATES;
					}
					
					for(leftOffset = 1; leftOffset < 5; leftOffset++)
					{
						if(x - leftOffset < 0) 
							continue;
						
						if(!world.getBlock(x - leftOffset, yChunk).isPassable() )
						{
							double leftLight = tmpLight - ((leftOffset - 1) * LIGHT_BLOCK_DISSIPATES);
							for(int j = yChunk; j < AVG_HEIGHT; j++)
							{	
								if(!world.getBlock(x - leftOffset, j).isPassable())
									leftLight -= LIGHT_BLOCK_DISSIPATES;
									
								double lightValue = 0.0F;
								
								if((lightValue = world.getAmbientLight(x - leftOffset, j)) < leftLight)
									world.setAmbientLight(x - leftOffset, j, leftLight);
								
								if(leftLight <= 0.0F)
								{
									break;
								}
							}
						}
					}
				
					for(rightOffset = 1; rightOffset < 5; rightOffset++)
					{
						if(x + rightOffset >= world.getWidth())
							continue;
						if(!world.getBlock(x + rightOffset, yChunk).isPassable() )
						{
							double rightLight = tmpLight - ((rightOffset - 1) * LIGHT_BLOCK_DISSIPATES);;
							for(int j = yChunk; j < AVG_HEIGHT; j++)
							{	
								if(!world.getBlock(x + rightOffset, j).isPassable())
									rightLight -= LIGHT_BLOCK_DISSIPATES;
														
								if(world.getAmbientLight(x + rightOffset, j) < rightLight)
									world.setAmbientLight(x + rightOffset, j, rightLight);
								
								if(rightLight <= 0.0F)
								{
									break;
								}
							}
							if(rightLight <= 0.0F || x + rightOffset >= world.getWidth() - 1)
							{
								break;
							}
						}
					}
					
					if(world.getAmbientLight(x, yChunk) < tmpLight)
						world.setAmbientLight(x, yChunk, tmpLight);
					
					yChunk++;
				}
				
				
			    world.getChunks().get(""+(int)(x / Chunk.getChunkWidth())).lightUpdated = false;
			}
			else if(world.getAmbientLight(x, y - 1) > 0)
			{
				double lightStrength = world.getAmbientLight(x, y - 1);
				
				int AVG_HEIGHT = world.getHeight() - 1;
				
				if(AVG_HEIGHT >= world.getHeight())
					AVG_HEIGHT = world.getHeight() - 1;
				//bounds are band-aid'd
				Block block = null;
				int xChunk = x;
	
				for(int i = 0; i < 1; i++)
				//for(int xChunk = x - AMBIENT_LIGHT_CLEAR_RADIUS; xChunk < x + AMBIENT_LIGHT_CLEAR_RADIUS; xChunk++)	
				{
					int yChunk = y;
					double tmpLight = lightStrength;
					int leftOffset = 1;
					int rightOffset = 1;
					
					while(yChunk < AVG_HEIGHT && tmpLight > 0.0F)
					{
						if(!(block = world.getBlock(xChunk, yChunk)).isPassable())
						{
							tmpLight -= LIGHT_BLOCK_DISSIPATES;
						}
						
						for(leftOffset = 1; leftOffset < 5; leftOffset++)
						{
							if(xChunk - leftOffset - 1 < 0) 
								continue;
							
							if(!world.getBlock(xChunk - leftOffset, yChunk).isPassable())
							{
								double leftLight = tmpLight - ((leftOffset-1) * LIGHT_BLOCK_DISSIPATES);
								for(int j = yChunk; j < AVG_HEIGHT; j++)
								{	
									if(!world.getBlock(xChunk - leftOffset, j).isPassable())
										leftLight -= LIGHT_BLOCK_DISSIPATES;
										
									double lightValue = 0.0F;
									
									if(!((lightValue = world.getAmbientLight(xChunk - leftOffset - 1, j)) > world.getAmbientLight(xChunk - leftOffset, j))  
									  && !((lightValue = world.getAmbientLight(xChunk - leftOffset, j - 1)) > world.getAmbientLight(xChunk - leftOffset, j)) 
									)
									{	
										world.setAmbientLight(xChunk - leftOffset, j, leftLight);
									}
									
									
								}
							}
						}
					
						for(rightOffset = 1; rightOffset < 5; rightOffset++)
						{
							if(xChunk + rightOffset + 1 >= world.getWidth())
								continue;
							if(!world.getBlock(xChunk + rightOffset, yChunk).isPassable() )
							{
								double rightLight = tmpLight - ((rightOffset - 1) * LIGHT_BLOCK_DISSIPATES);;
								for(int j = yChunk; j < AVG_HEIGHT; j++)
								{	
									if(!world.getBlock(xChunk + rightOffset, j).isPassable())
										rightLight -= LIGHT_BLOCK_DISSIPATES;
															
									if(!(world.getAmbientLight(xChunk + rightOffset + 1, j) > world.getAmbientLight(xChunk + rightOffset, j)))
									{
										world.setAmbientLight(xChunk + rightOffset, j, rightLight);
									}
									
									if(rightLight <= 0.0F)
									{
										break;
									}
								}
								if(rightLight <= 0.0F || xChunk + rightOffset >= world.getWidth() - 1)
								{
									break;
								}
							}
						}
						
						//if(world.getAmbientLight(xChunk, yChunk) < tmpLight)
							world.setAmbientLight(xChunk, yChunk, tmpLight);
						
						yChunk++;
					}
					
					
				    world.getChunks().get(""+(int)(xChunk / Chunk.getChunkWidth())).lightUpdated = false;//updateChunkLight();
					
				
				}
			}
			*/
		}
	}
	
	//Excludes second x,y values
	public void fixDiffuseLightRemove(World world, final int x, final int y, final int xExclude, final int yExclude)
	{
		for(int i = x - DIFFUSE_LIGHT_CHECK_RADIUS; i < x + DIFFUSE_LIGHT_CHECK_RADIUS; i++)
		{
			for(int k = y - DIFFUSE_LIGHT_CHECK_RADIUS; k < y + DIFFUSE_LIGHT_CHECK_RADIUS; k++)
			{
				if(i >= world.getWidth() || i < 0 || k < 0 || k >= world.getHeight() || (i == xExclude && k == yExclude))
				{
					continue;
				}
				
				if(world.getBlock(i, k) instanceof BlockLight)
				{
					BlockLight block = (BlockLight)world.getBlock(i, k);
					removeLightSource(world, i, k, block.lightRadius, block.lightStrength);
					//applyLightSource(world, i, k, block.lightRadius, block.lightStrength);				
				}
			}
		}
	}
	
	public void fixDiffuseLightRemove(World world, final int x, final int y)
	{
		for(int i = x - DIFFUSE_LIGHT_CHECK_RADIUS; i < x + DIFFUSE_LIGHT_CHECK_RADIUS; i++)
		{
			for(int k = y - DIFFUSE_LIGHT_CHECK_RADIUS; k < y + DIFFUSE_LIGHT_CHECK_RADIUS; k++)
			{
				if(i >= world.getWidth() || i < 0 || k < 0 || k >= world.getHeight())
				{
					continue;
				}
				
				if(world.getBlock(i, k) instanceof BlockLight)
				{
					BlockLight block = (BlockLight)world.getBlock(i, k);
					removeLightSource(world, i, k, block.lightRadius, block.lightStrength);
					//applyLightSource(world, i, k, block.lightRadius, block.lightStrength);				
				}
			}
		}
	}
	
	public void fixDiffuseLightApply(World world, final int x, final int y)
	{
		for(int i = x - DIFFUSE_LIGHT_CHECK_RADIUS; i < x + DIFFUSE_LIGHT_CHECK_RADIUS; i++)
		{
			for(int k = y - DIFFUSE_LIGHT_CHECK_RADIUS; k < y + DIFFUSE_LIGHT_CHECK_RADIUS; k++)
			{
				if(i >= world.getWidth() || i < 0 || k < 0 || k >= world.getHeight())
				{
					continue;
				}
				
				if(world.getBlock(i, k) instanceof BlockLight)
				{
					BlockLight block = (BlockLight)world.getBlock(i, k);
					//removeLightSource(world, i, k, block.lightRadius, block.lightStrength);
					applyLightSource(world, i, k, block.lightRadius, block.lightStrength);				
				}
			}
		}
		
	}
	
	private void ambientUpdateBlockBreak(World world, int x, int y)
	{
		if(y == 0) 
		{
			double lightStrength = world.getLightLevel();
			
			int AVG_HEIGHT = world.getHeight() - 1;
			
			if(AVG_HEIGHT >= world.getHeight())
				AVG_HEIGHT = world.getHeight() - 1;
			//bounds are band-aid'd
			
			int yChunk = 1;
			double tmpLight = lightStrength;
			int leftOffset = 1;
			int rightOffset = 1;
			
			while(yChunk < AVG_HEIGHT && tmpLight > 0.0F)
			{
				if(world.getBlock(x, yChunk).getIsSolid())
				{
					tmpLight -= LIGHT_BLOCK_DISSIPATES;
				}
				
				for(leftOffset = 1; leftOffset < 5; leftOffset++)
				{
					if(x - leftOffset < 0) 
						continue;
					
					if(world.getBlock(x - leftOffset, yChunk).getIsSolid() /*&& solid[x - leftOffset][yChunk + 1] == 0*/)
					{
						double leftLight = tmpLight - ((leftOffset - 1) * LIGHT_BLOCK_DISSIPATES);
						for(int j = yChunk; j < AVG_HEIGHT; j++)
						{	
							if(world.getBlock(x - leftOffset, j).getIsSolid())
								leftLight -= LIGHT_BLOCK_DISSIPATES;
													
							if(world.getAmbientLight(x - leftOffset, j) < leftLight)
								world.setAmbientLight(x - leftOffset, j, leftLight);
							
							if(leftLight <= 0.0F)
							{
								break;
							}
						}
					}
				}
			
				for(rightOffset = 1; rightOffset < 5; rightOffset++)
				{
					if(x + rightOffset >= world.getWidth())
						continue;
					if(world.getBlock(x + rightOffset, yChunk).getIsSolid() /* && solid[x + rightOffset][yChunk + 1] == 0 */)
					{
						double rightLight = tmpLight - ((rightOffset - 1) * LIGHT_BLOCK_DISSIPATES);;
						for(int j = yChunk; j < AVG_HEIGHT; j++)
						{	
							if(world.getBlock(x + rightOffset, j).getIsSolid())
								rightLight -= LIGHT_BLOCK_DISSIPATES;
													
							if(world.getAmbientLight(x + rightOffset, j) < rightLight)
								world.setAmbientLight(x + rightOffset, j, rightLight);
							
							if(rightLight <= 0.0F)
							{
								break;
							}
						}
						if(rightLight <= 0.0F || x + rightOffset >= world.getWidth() - 1)
						{
							break;
						}
					}
				}
				
				if(world.getAmbientLight(x, yChunk) < tmpLight)
					world.setAmbientLight(x, yChunk, tmpLight);
				
				yChunk++;
			}
			
			
		    world.getChunks().get(""+(int)(x / Chunk.getChunkWidth())).setLightUpdated(false);
		}
		else if(world.getAmbientLight(x, y - 1) > 0)
		{
			double lightStrength = world.getAmbientLight(x, y - 1);
			
			int AVG_HEIGHT = world.getHeight() - 1;
			
			if(AVG_HEIGHT >= world.getHeight())
				AVG_HEIGHT = world.getHeight() - 1;
			//bounds are band-aid'd
			
			int yChunk = y - 1;
			double tmpLight = lightStrength;
			int leftOffset = 1;
			int rightOffset = 1;
			
			while(yChunk < AVG_HEIGHT && tmpLight > 0.0F)
			{
				if(world.getBlock(x, yChunk).getIsSolid())
				{
					tmpLight -= LIGHT_BLOCK_DISSIPATES;
				}
				
				for(leftOffset = 1; leftOffset < 5; leftOffset++)
				{
					if(x - leftOffset < 0) 
						continue;
					
					if(world.getBlock(x - leftOffset, yChunk).getIsSolid() /*&& solid[x - leftOffset][yChunk + 1] == 0*/)
					{
						double leftLight = tmpLight - ((leftOffset - 1) * LIGHT_BLOCK_DISSIPATES);
						for(int j = yChunk; j < AVG_HEIGHT; j++)
						{	
							if(world.getBlock(x - leftOffset, j).getIsSolid())
								leftLight -= LIGHT_BLOCK_DISSIPATES;
													
							if(world.getAmbientLight(x - leftOffset, j) < leftLight)
								world.setAmbientLight(x - leftOffset, j, leftLight);
							
							if(leftLight <= 0.0F)
							{
								break;
							}
						}
					}
				}
			
				for(rightOffset = 1; rightOffset < 5; rightOffset++)
				{
					if(x + rightOffset >= world.getWidth())
						continue;
					if(world.getBlock(x + rightOffset, yChunk).getIsSolid() /* && solid[x + rightOffset][yChunk + 1] == 0 */)
					{
						double rightLight = tmpLight - ((rightOffset - 1) * LIGHT_BLOCK_DISSIPATES);;
						for(int j = yChunk; j < AVG_HEIGHT; j++)
						{	
							if(world.getBlock(x + rightOffset, j).getIsSolid())
								rightLight -= LIGHT_BLOCK_DISSIPATES;
													
							if(world.getAmbientLight(x + rightOffset, j) < rightLight)
								world.setAmbientLight(x + rightOffset, j, rightLight);
							
							if(rightLight <= 0.0F)
							{
								break;
							}
						}
						if(rightLight <= 0.0F || x + rightOffset >= world.getWidth() - 1)
						{
							break;
						}
					}
				}
				
				if(world.getAmbientLight(x, yChunk) < tmpLight)
					world.setAmbientLight(x, yChunk, tmpLight);
				
				yChunk++;
			}
			
			
		    world.getChunks().get(""+(int)(x / Chunk.getChunkWidth())).setLightUpdated(false);
			
			
		}	
	}
	
	private double sat(double f)
	{
		return (f > 1.0F) ? 1.0F : (f < 0.0F) ? 0.0F : f;
	}
	
	private Vector2 subtractVector(int x1, int y1, int x2, int y2)
	{
		return new Vector2(x1 - x2, y1 - y2);
	}
}
