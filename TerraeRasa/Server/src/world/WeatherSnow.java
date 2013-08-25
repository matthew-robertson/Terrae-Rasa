package world;

import java.util.Random;

import utils.MathHelper;
import utils.Particle;
import blocks.Block;

public class WeatherSnow extends Weather
{
	public Particle[] snow;
	private Random random = new Random();
	/**
	 * Gives the WeatherSnow instance all the information it needs to create a decent snow effect
	 * then initializes all the particles appropriately. 
	 * @param biome the biome the weather is occuring in
	 * @param averageGroundLevel the average ground level of the world (pre-calculated)
	 */
	public WeatherSnow(World world, Biome biome, int averageGroundLevel)
	{
		this.weatherLocation = biome;
		this.x = (int) biome.getX();
		this.width = (int) biome.getWidth();
		this.height = (int) biome.getHeight();
		this.y = (int) biome.getY();
		this.averageGroundLevel = averageGroundLevel;
		this.snow = new Particle[(int) (width * 2.75f)];
		initialize(world);
	}
	
	/**
	 * Gets the specified block in the worldMap. Used because it's a lot cleaner and shorter than typing out a bounds 
	 * safe worldMap request
	 * @param x the x position in worldMap
	 * @param y the y postion in worldMap
	 * @return the Block at the specified position
	 */
	private Block getBlockAtPosition(World world, double x, double y)
	{
		return world.getBlockGenerate(MathHelper.returnIntegerInWorldMapBounds_X(world, (int)x / 6), MathHelper.returnIntegerInWorldMapBounds_X(world, (int)y / 6));
	}
	
	/**
	 * Gets the specified block's isPassable() in the worldMap. Used because it's a lot cleaner and shorter than typing out a bounds 
	 * safe worldMap request. 
	 * @param x the x position in worldMap
	 * @param y the y postion in worldMap
	 * @return the Block at the specified position
	 */
	private boolean isInBlock(World world, double x, double y)
	{
		return (world.getBlockGenerate(MathHelper.returnIntegerInWorldMapBounds_X(world, (int)x / 6), MathHelper.returnIntegerInWorldMapBounds_X(world, (int)y / 6)).getIsSolid());
	}
	
	/**
	 * Initializes all the particles, and weather. Gives the storm a duration of 10-15 minutes (12000-18000 ticks)
	 */
	public void initialize(World world)
	{
		ticksLeft = 12000 + random.nextInt(6000);
		
		for(int i = 0; i < snow.length; i++)
		{
			snow[i] = new Particle();
			snow[i].x = (random.nextInt(width) + x) * 6;
			snow[i].y = (random.nextInt(40) + averageGroundLevel - 140) * 6;
		}
	}
	
	/**
	 * Gets the Particle[] for the weather instance
	 * @return the Particle[] currently being used for the specified weather
	 */
	public Particle[] getSnow()
	{
		return snow;
	}
	
	/**
	 * Updates all the particles (applies gravity). Additionally, if a snow particle hits a block then a snow cover will be applied, or the block will
	 * be converted to snowy grass
	 */
	public void update(World world)
	{
		for(int i = 0; i < snow.length; i++)
		{
			snow[i].y += 0.2f; //apply gravity
			
			if(isInBlock(world, snow[i].x, snow[i].y)) //if the particle hits something, add snow and reset the particle
			{
				//so this is currently being put on hold, but eventually this should be converted to work for anything.
				if(getBlockAtPosition(world, snow[i].x, snow[i].y).isSolid && snow[i].x > 0 && snow[i].y > 0)
				{
					
					//Adding snow cover
					if(world.getBlockGenerate(MathHelper.returnIntegerInWorldMapBounds_X(world, (int)snow[i].x / 6), 
							MathHelper.returnIntegerInWorldMapBounds_Y(world, (int)((snow[i].y - 6) / 6))).getID() == Block.air.getID())
					{
						world.setBlockGenerate(Block.snowCover, 
								MathHelper.returnIntegerInWorldMapBounds_X(world, (int)snow[i].x / 6), 
								MathHelper.returnIntegerInWorldMapBounds_Y(world, (int)((snow[i].y - 6) / 6)));						
					}
					
					Block block = world.getBlockGenerate(MathHelper.returnIntegerInWorldMapBounds_X(world, (int)snow[i].x / 6),
							MathHelper.returnIntegerInWorldMapBounds_Y(world, (int)((snow[i].y - 6) / 6) + 1));
					
					//Converting grass to snowy-grass:
					if (block.getID() == Block.grass.getID() && block.getBitMap() < 16)
					{
						int x = MathHelper.returnIntegerInWorldMapBounds_X(world, (int)(snow[i].x / 6));
						int y = MathHelper.returnIntegerInWorldMapBounds_Y(world, (int)((snow[i].y - 6) / 6) + 1);
						Block requestedBlock = world.getBlockGenerate(x, y);
						world.setBitMap(x, y, requestedBlock.getBitMap() + 16);					
					}					
					
				}
				
				snow[i].x = (random.nextInt(width) + x) * 6;
				snow[i].y = (random.nextInt(25) + averageGroundLevel - 40) * 6;
			}
		} 	
	}
}
