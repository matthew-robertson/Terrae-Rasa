package client.render;



import java.util.Enumeration;

import blocks.ChunkClient;

import client.world.WeatherSnow;
import client.world.WorldClientEarth;


public class RenderParticles extends Render
{
	/**
	 * Attempts to render (and then update) the weather stored in the world object
	 */
	public void render(WorldClientEarth world)
	{
		Enumeration<String> keys = world.getChunks().keys();
        while (keys.hasMoreElements()) 
        {
            ChunkClient chunk = world.getChunks().get((String)(keys.nextElement()));
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
