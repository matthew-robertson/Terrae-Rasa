package render;

import io.Chunk;

import java.util.Enumeration;

import world.WeatherSnow;
import world.World;

public class RenderParticles extends Render
{
	/**
	 * Attempts to render (and then update) the weather stored in the world object
	 */
	public void render(World world)
	{
		Enumeration<String> keys = world.getChunks().keys();
        while (keys.hasMoreElements()) 
        {
            Chunk chunk = (Chunk)world.getChunks().get((String)(keys.nextElement()));
            if(chunk.weather != null)
            {
        		if(chunk.weather instanceof WeatherSnow) //if there's snow, render it and call a weather update from world
        		{
        			renderSnow((WeatherSnow)chunk.weather);
        		}
        	}
        }
	}
	
	/**
	 * Draw all particles contained in world.weather. In this case the particles must be of type snow.
	 */
	public void renderSnow(WeatherSnow weather)
	{
		WeatherSnow weatherSnow = weather;		
		snow_tex.bind();
		final int size = 2; //Ortho size of the particles
		
		t.startDrawingQuads(); 
		for(int i = 0; i < weatherSnow.snow.length; i++) //Draw all the snow particles
		{
			double x = weatherSnow.snow[i].x;
			double y = weatherSnow.snow[i].y;
			t.addVertexWithUV(x , y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
		}
		t.draw();
	}
}
