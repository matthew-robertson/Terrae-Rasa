package items;


public class ItemTool extends Item
{
	/** Damage the weapon does */
	public int damageDone; 	
	public double[] xBounds;
	public double[] yBounds;
	//Render size - ortho (1/2 pixel)
	public double size;
	/** The swing time of the tool in seconds*/
	public double swingTime;
	public String swingSound;
	public String hitSound;
		
	protected ItemTool(int i, String hit)
	{
		super(i);
		damageDone = 1;
		maxStackSize = 1;
		setXBounds(new double[] { 13F/16, 16F/16, 16F/16, 4F/16, 1F/16 });
		setYBounds(new double[] { 1F/16, 1F/16 ,  4F/16, 16F/16, 13F/16 });		
		size = 20;
		swingTime = 3.0;
		hitSound = hit;
	}
	
	/**
	 * -- In sec
	 * @param speed
	 * @return
	 */
	protected ItemTool setSwingTime(double speed)
	{
		this.swingTime = speed;
		return this;
	}
		
	protected ItemTool setSize(int size)
	{
		this.size = size;
		return this;
	}
	
	protected ItemTool setDamageDone(int i)
	{
		damageDone = i;
		return this;
	}
	
	public int getDamageDone()
	{
		return damageDone;
	}
	
	protected ItemTool setXBounds(double[] bounds)
	{
		this.xBounds = bounds;
		return this;
	}
	
	protected ItemTool setYBounds(double[] bounds)
	{
		this.yBounds = bounds;
		return this;
	}
	
	/**
	 * Gets the stats of the given item. ItemTools should have at least a damage value.
	 * @return an array of this ItemTool's stats, which should be at least of size 1
	 */
	public String[] getStats()
	{
		return new String[] { "Damage: "+damageDone };
	}
}
