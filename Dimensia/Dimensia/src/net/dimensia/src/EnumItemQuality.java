package net.dimensia.src;

/**
 * Item Quality constants with RGBA values for their tooltips, using floats of value 0.0F to 1.0F. Call
 * {@link #getAsArray()} on an EnumItemQuality to get an array of the colours in format { r, g, b, a } or
 * request the properties individually using dot notation.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumItemQuality 
{
	COMMON(1.0F, 1.0F, 1.0F, 1.0F),
	RARE(0.0F, 0.0F, 1.0F, 1.0F),
	LEGENDARY(1.0F, 215.0F/255.0F, 0.0F, 1.0F);
	
	public float r;
	public float g;
	public float b;
	public float a;
	
	EnumItemQuality(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	/**
	 * Gets the colour of the item as a float array
	 * @return a float array of format { r, g, b, a }
	 */
	public float[] getAsArray()
	{
		return new float[] { r, g, b, a };
	}	
}
