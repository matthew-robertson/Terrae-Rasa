package enums;

public enum EnumColor 
{
	RED(1, new double[] { 1, 0, 0, 1 }), 
	GREEN(2, new double[] { 0, 1, 0, 1 }), 
	WHITE(3, new double[] { 1, 1, 1, 1 }),
	CRITICAL(4, new double[] { 1, 0.5f, 0, 1 }),
	BLUE(5, new double[] { 0, 0, 1, 1 }), 
	GRAY(6, new double[] { 0.66666666f, 0.66666666f, 0.66666666f, 1 });		

	public final double[] COLOR;
	
	EnumColor(int i, double[] colour)
	{
		this.COLOR = colour;
	}
}
