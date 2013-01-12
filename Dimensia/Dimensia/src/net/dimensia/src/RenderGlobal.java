package net.dimensia.src;


public class RenderGlobal extends Render
{
	public static void render(World world)
	{
		renderWorld.render(world);
	}
	
	private static RenderWorld renderWorld = new RenderWorld();
}
