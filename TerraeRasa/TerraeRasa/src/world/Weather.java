package world;


import java.io.Serializable;

import blocks.Chunk;

import transmission.ServerUpdate;
import utils.WeatherData;
import client.world.WeatherSnow;

//TODO: weather is a bit borked.
public abstract class Weather 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected int ticksLeft;
	protected int averageGroundLevel;
	protected int y;
	protected int height;
	protected int x;
	protected int width;
	
	public abstract void update(World world, ServerUpdate update);
	
	public abstract void initialize();
	
	/**
	 * Decreases game ticks left for the weather effect by the specified amount
	 * @param i the number of ticks to reduce ticksLeft by
	 */
	public void reduceTicksLeft(int i)
	{
		ticksLeft -= i;
	}
	
	/**
	 * Determines if the weather effect is done.
	 * @return true if the effect is done, otherwise false
	 */
	public boolean isFinished()
	{
		return ticksLeft <= 0;
	}
	
	public void setTicksLeft(int ticksLeft)
	{
		this.ticksLeft = ticksLeft;
	}
	
	public WeatherData getData()
	{
		WeatherData data = new WeatherData();
		data.x = x;
		data.y = y;
		data.height = height;
		data.width = width;
		data.averageGroundLevel = averageGroundLevel;
		data.ticksLeft = ticksLeft;
		data.id = getID();
		return data;
	}
	//Client only
	public static Weather generateWeatherByID(int id, Chunk chunk, int averageWorldHeight)
	{
		if(id == WeatherSnow.ID)
		{
			//TODO fix!
			return new client.world.WeatherSnow(chunk, averageWorldHeight);
		}
		return null;
	}
	//Client only
	public static Weather generateWeatherByID(WeatherData data)
	{
		if(data.id == WeatherSnow.ID)
		{
			return new client.world.WeatherSnow(data);
		}
		return null;
	}
	
	
	public abstract int getID();
}
