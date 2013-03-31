package net.dimensia.src;

public enum EnumColor 
{
	RED(1, new float[] { 1, 0, 0, 1 }), 
	GREEN(2, new float[] { 0, 1, 0, 1 }), 
	WHITE(3, new float[] { 1, 1, 1, 1 }),
	CRITICAL(4, new float[] { 1, 0.5f, 0, 1 }),
	BLUE(5, new float[] { 0, 0, 1, 1 }), 
	GRAY(6, new float[] { 0.66666666f, 0.66666666f, 0.66666666f, 1 });		

	public final float[] COLOR;
	
	EnumColor(int i, float[] colour)
	{
		this.COLOR = colour;
	}
}
