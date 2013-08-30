package world;

import java.io.Serializable;

import transmission.ServerUpdate;

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
	protected Biome weatherLocation;
	
	public abstract void update(World world, ServerUpdate update);
	
	public abstract void initialize(World world);
	
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
	
	public abstract int getID();
}
