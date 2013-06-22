package blocks;

public class BlockLight extends Block
{
	private static final long serialVersionUID = 1L;
	public double lightStrength;
	public int lightRadius;
	
	protected BlockLight(int i)
	{
		super(i);
		lightStrength = 1.0F;
		lightRadius = 1;
	}
	
	/**
	 * Set the light strength and radius
	 * @param strength the max strength of the light. 1.0F is 100%, 0.0F is 0%
	 * @param radius the max distance of the light in blocks
	 * @return the updated block
	 */
	public Block setLightStrengthAndRadius(double strength, int radius)
	{
		this.lightStrength = strength;
		this.lightRadius = radius;
		return this;
	}
	
	public double getLightStrength()
	{
		return lightStrength;
	}
	
	public double getLightRadius()
	{
		return lightRadius;
	}
}
