package net.dimensia.src;

public class RenderParticles extends Render
{
	/**
	 * Attempts to render (and then update) the weather stored in the world object
	 */
	public void render(World world)
	{
		if(world.weather == null) //if there's no weather, prevent an exception
		{
			return;
		}
		
		if(world.weather instanceof WeatherSnow) //if there's snow, render it and call a weather update from world
		{
			renderSnow(world);
			world.updateWeather();
		}
	}
	
	/**
	 * Draw all particles contained in world.weather. In this case the particles must be of type snow.
	 */
	public void renderSnow(World world)
	{
		WeatherSnow weatherSnow = (WeatherSnow) world.weather;		
		snow_tex.bind();
		final int size = 2; //Ortho size of the particles
		
		t.startDrawingQuads(); 
		for(int i = 0; i < weatherSnow.snow.length; i++) //Draw all the snow particles
		{
			float x = weatherSnow.snow[i].x;
			float y = weatherSnow.snow[i].y;
			t.addVertexWithUV(x , y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
		}
		t.draw();
	}
}
