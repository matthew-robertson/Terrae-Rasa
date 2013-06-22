package world;

import enums.EnumDifficulty;

/**
 * Extends World, which is currently the overworld and not a true base class. 
 * May later provide increased functionality, currently is basically just the World class again.
 */
public class WorldHell extends World
{
	private static final long serialVersionUID = 1L;

	public WorldHell()
	{
		super();
		
	}
	
	public WorldHell(String universeName, int width, int height, EnumDifficulty difficulty)
	{
		super(universeName, width, height, difficulty);
		worldName = "Hell";
	}
}
