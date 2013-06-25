package world;

import enums.EnumWorldDifficulty;

/**
 * Extends World, which is currently the overworld and not a true base class. 
 * May later provide increased functionality, currently is basically just the World class again.
 */
public class WorldSky extends World
{
	public WorldSky()
	{
		super();
	}
	
	public WorldSky(String name, int width, int height, EnumWorldDifficulty difficulty)
	{
		super(name, width, height, difficulty);
	}
}