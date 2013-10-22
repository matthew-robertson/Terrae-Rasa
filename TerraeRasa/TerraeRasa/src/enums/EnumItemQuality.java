package enums;

/**
 * EnumItemQuality holds Item quality constants with RGBA values for their tooltips, 
 * using doubles of value 0.0F to 1.0F. Call {@link #getAsArray()} on an 
 * EnumItemQuality to get an array of the colours in format { r, g, b, a } or
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
	
	/** This EnumItemQuality's r component of RGBA, as a value between 0 and 1. */
	private double r;
	/** This EnumItemQuality's g component of RGBA, as a value between 0 and 1. */
	private double g;
	/** This EnumItemQuality's b component of RGBA, as a value between 0 and 1. */
	private double b;
	/** This EnumItemQuality's a component of RGBA, as a value between 0 and 1. */
	private double a;
	
	/**
	 * Constructs a new EnumItemQuality with given colours.
	 * @param r the r component of RGBA to assign to this EnumItemQuality, as a value between 0 and 1
	 * @param g the g component of RGBA to assign to this EnumItemQuality, as a value between 0 and 1
	 * @param b the b component of RGBA to assign to this EnumItemQuality, as a value between 0 and 1
	 * @param a the a component of RGBA to assign to this EnumItemQuality, as a value between 0 and 1
	 */
	EnumItemQuality(double r, double g, double b, double a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	/**
	 * Gets the colour of the item as a double array in format { r, g, b, a }
	 * @return a double array of format { r, g, b, a }
	 */
	public double[] getAsArray()
	{
		return new double[] { getR(), g, b, a };
	}

	public double getR() {
		return r;
	}
	
	public double getG() {
		return g;
	}
	
	public double getB() {
		return b;
	}

}
