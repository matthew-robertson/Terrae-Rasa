package net.dimensia.src;

/**
 * Extends World, which is currently the overworld and not a true base class. 
 * May later provide increased functionality, currently is basically just the World class again.
 */
public class WorldSky extends World
{
	private static final long serialVersionUID = 1L;

	public WorldSky()
	{
		super();
	}
	
	public WorldSky(String name, int width, int height, EnumDifficulty difficulty)
	{
		super(name, width, height, difficulty);
	}
}