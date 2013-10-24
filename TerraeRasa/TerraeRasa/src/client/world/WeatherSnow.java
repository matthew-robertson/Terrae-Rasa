package client.world;



import java.util.Random;

import math.MathHelper;
import transmission.ServerUpdate;
import utils.Particle;
import utils.WeatherData;
import world.Weather;
import world.World;
import blocks.Chunk;


public class WeatherSnow extends Weather
{
	private static final long serialVersionUID = 1L;
	public Particle[] snow;
	private Random random = new Random();
	public static final int ID = 1;
	
	public WeatherSnow(WeatherData data)
	{
		this.x = data.x;
		this.y = data.y;
		this.averageGroundLevel = data.averageGroundLevel;
		this.ticksLeft = data.ticksLeft;
		this.width = data.width;
		this.height = data.height;
	}
	
	/**
	 * Gives the WeatherSnow instance all the information it needs to create a decent snow effect
	 * then initializes all the particles appropriately. 
	 * @param biome the biome the weather is occuring in
	 * @param averageGroundLevel the average ground level of the world (pre-calculated)
	 */
	public WeatherSnow(Chunk chunk, int averageGroundLevel)
	{
		this.x = (int) chunk.getX() * Chunk.getChunkWidth(); 
		this.width = (int) Chunk.getChunkWidth();
		this.height = (int) chunk.getHeight();
		this.y = 0;
		this.averageGroundLevel = averageGroundLevel;
		this.snow = new Particle[(int) (width * 2.75f)];
		initialize();
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
		return (world.getAssociatedBlock(MathHelper.returnIntegerInWorldMapBounds_X(world, (int)x / 6), MathHelper.returnIntegerInWorldMapBounds_X(world, (int)y / 6)).getIsSolid());
	}
	
	/**
	 * Initializes all the particles, and weather. Gives the storm a duration of 10-15 minutes (12000-18000 ticks)
	 */
	public void initialize()
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
				snow[i].x = (random.nextInt(width) + x) * 6;
				snow[i].y = (random.nextInt(25) + averageGroundLevel - 40) * 6;
			}
		} 	
	}

	public void update(World world, ServerUpdate update) {
		
	}

	@Override
	public int getID() {
		return ID;
	}
}
