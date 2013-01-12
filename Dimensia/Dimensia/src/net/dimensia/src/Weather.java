package net.dimensia.src;


public abstract class Weather 
{
	public abstract void update(World world);
	
	public abstract void initialize(World world);
	
	/**
	 * Decreases game ticks left for the weather effect by the specified amount
	 * @param i the number of ticks to reduce ticksLeft by
	 */
	public void reduceTicksLeft(int i)
	{
		ticksLeft -= i;
	}
	
	protected int ticksLeft;
	protected int averageGroundLevel;
	protected int y;
	protected int height;
	protected int x;
	protected int width;
	protected Biome weatherLocation;
}
