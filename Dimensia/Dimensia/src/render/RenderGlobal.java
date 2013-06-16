package render;
import client.GameEngine;
import client.Settings;
import entities.EntityPlayer;
import world.World;

public class RenderGlobal extends Render
{
	private static RenderWorldEarth renderWorldEarth = new RenderWorldEarth();
	private static RenderWorldHell renderWorldHell = new RenderWorldHell();
	private static RenderWorldSky renderWorldSky = new RenderWorldSky();
	
	/**
	 * Renders the world, based on dimension. The value of renderMode is defined in GameEngine.java
	 * @param world the world used to render, generally an extension of the world base-class
	 * @param player the player object for the given client, or singleplayer game
	 * @param renderMode describes the world being rendered, based on the values in GameEngine.java
	 */
	public static void render(World world, EntityPlayer player, int renderMode, Settings settings)
	{
		if(renderMode == GameEngine.RENDER_MODE_WORLD_EARTH)
		{
			renderWorldEarth.render(world, player, settings);
		}
		else if(renderMode == GameEngine.RENDER_MODE_WORLD_HELL)
		{
			renderWorldHell.render(world, player, settings);
		}
		else if(renderMode == GameEngine.RENDER_MODE_WORLD_SKY)
		{
			renderWorldSky.render(world, player, settings);
		}
	}
}
