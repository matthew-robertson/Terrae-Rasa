package world;

import enums.EnumWorldDifficulty;

/**
 * Extends World, which is currently the overworld and not a true base class. 
 * May later provide increased functionality, currently is basically just the World class again.
 */
public class WorldHell extends World
{
	public WorldHell()
	{
		super();
		
	}
	
	public WorldHell(String universeName, int width, int height, EnumWorldDifficulty difficulty)
	{
		super(universeName, width, height, difficulty);
		worldName = "Hell";
	}
}
