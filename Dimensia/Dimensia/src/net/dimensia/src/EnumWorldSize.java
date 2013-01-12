package net.dimensia.src;

public enum EnumWorldSize
{
	SMALL(2400, 1200), 
	MEDIUM(3600, 1800), 
	LARGE(4800, 2400); 
	
	EnumWorldSize(int w, int h)
	{
		width = w;
		height = h;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public static EnumWorldSize getSize(String s)
	{
		s = s.toLowerCase();
		if(s.equals("small"))
		{
			return EnumWorldSize.SMALL;
		}
		if(s.equals("medium"))
		{
			return EnumWorldSize.MEDIUM;
		}
		if(s.equals("large"))
		{
			return EnumWorldSize.LARGE;
		}
		return EnumWorldSize.MEDIUM;
	}
	
	private int width;
	private int height;
}
