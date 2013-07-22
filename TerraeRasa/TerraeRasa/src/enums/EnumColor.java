package enums;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Vector;

public enum EnumColor 
{
	RED("RED", 1, new double[] { 1, 0, 0, 1 }), 
	GREEN("GREEN", 2, new double[] { 0, 1, 0, 1 }), 
	WHITE("WHITE", 3, new double[] { 1, 1, 1, 1 }),
	CRITICAL("CRITICAL", 4, new double[] { 1, 0.5f, 0, 1 }),
	BLUE("BLUE", 5, new double[] { 0, 0, 1, 1 }), 
	GRAY("GRAY", 6, new double[] { 0.66666666f, 0.66666666f, 0.66666666f, 1 }),		
	DARK_GRAY("DARK_GRAY", 7, new double[] { 64.0/255.0, 64.0/255.0, 64.0/255.0, 1 }),
	LIGHT_GREEN("LIGHT_GREEN", 8, new double[] { 152.0 / 255.0, 251.0/255.0, 152.0/255.0, 1 }), 
	LIME_GREEN("LIME_GREEN", 9, new double[] { 50.0/255, 205.0/255, 50.0/255, 1}),
	FIERY1("FIERY1", 10, new double[] { 255.0/255, 253.0/255, 24.0/255, 1}),
	FIERY2("FIERY2", 11, new double[] { 253.0/255, 124.0/255, 5.0/255, 1}),
	FIERY3("FIERY3", 12, new double[] { 253.0/255, 181.0/255, 9.0/255, 1}),
	FIERY4("FIERY4", 13, new double[] { 255.0/255, 117.0/255, 4.0/255, 1}),
	FIERY5("FIERY5", 14, new double[] { 255.0/255, 69.0/255, 0.0/255, 1}),
	YELLOW("YELLOW", 15, new double[] { 255.0/255, 255.0/255, 51.0/255, 1});

	public final double[] COLOR;
	private final String name;
	private static Vector<EnumColor> colors;
	static
	{
		colors = new Vector<EnumColor>();
		for (EnumColor tier: EnumSet.allOf(EnumColor.class))
        {
			colors.add(tier);
        }
	}
	
	EnumColor(String name, int i, double[] colour)
	{
		this.name = name;
		this.COLOR = colour;
	}
	
	public static EnumColor get(String name)
	{
		Iterator<EnumColor> it = colors.iterator();
		while(it.hasNext())
		{
			EnumColor color = it.next();
			if(color.toString().equals(name))
			{
				return color;
			}			
		}
		return WHITE;
	}
	
	public String toString()
	{
		return name;
	}
	
}
