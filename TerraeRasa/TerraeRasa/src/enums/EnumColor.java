package enums;

public enum EnumColor 
{
	RED(1, new double[] { 1, 0, 0, 1 }), 
	GREEN(2, new double[] { 0, 1, 0, 1 }), 
	WHITE(3, new double[] { 1, 1, 1, 1 }),
	CRITICAL(4, new double[] { 1, 0.5f, 0, 1 }),
	BLUE(5, new double[] { 0, 0, 1, 1 }), 
	GRAY(6, new double[] { 0.66666666f, 0.66666666f, 0.66666666f, 1 }),		
	DARK_GRAY(7, new double[] { 64.0/255.0, 64.0/255.0, 64.0/255.0, 1 }),
	LIGHT_GREEN(8, new double[] { 152.0 / 255.0, 251.0/255.0, 152.0/255.0, 1 }), 
	LIME_GREEN(9, new double[] { 50.0/255, 205.0/255, 50.0/255, 1});
	
	
	public final double[] COLOR;
	
	EnumColor(int i, double[] colour)
	{
		this.COLOR = colour;
	}
}
