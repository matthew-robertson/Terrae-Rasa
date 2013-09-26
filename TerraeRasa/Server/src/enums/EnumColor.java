package enums;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * EnumColor defines a bunch of miscellaneous colour constants that might be useful throughout the code.
 * Each EnumColor is given a plaintext name, and double[] containing values from 0 to 1 in the form of
 * { r, g, b, a }.
 * @author Alec Sobeck
 * @author Matthew Robertson
 * @version 1.0
 * @since 1.0
 */
public enum EnumColor 
{
	RED("RED", new double[] { 1, 0, 0, 1 }), 
	GREEN("GREEN", new double[] { 0, 1, 0, 1 }), 
	WHITE("WHITE", new double[] { 1, 1, 1, 1 }),
	CRITICAL("CRITICAL", new double[] { 1, 0.5f, 0, 1 }),
	BLUE("BLUE", new double[] { 0, 0, 1, 1 }), 
	GRAY("GRAY", new double[] { 0.66666666f, 0.66666666f, 0.66666666f, 1 }),		
	DARK_GRAY("DARK_GRAY", new double[] { 64.0/255.0, 64.0/255.0, 64.0/255.0, 1 }),
	LIGHT_GREEN("LIGHT_GREEN", new double[] { 152.0 / 255.0, 251.0/255.0, 152.0/255.0, 1 }), 
	LIME_GREEN("LIME_GREEN", new double[] { 50.0/255, 205.0/255, 50.0/255, 1}),
	FIERY1("FIERY1", new double[] { 255.0/255, 253.0/255, 24.0/255, 1}),
	FIERY2("FIERY2", new double[] { 253.0/255, 124.0/255, 5.0/255, 1}),
	FIERY3("FIERY3", new double[] { 253.0/255, 181.0/255, 9.0/255, 1}),
	FIERY4("FIERY4", new double[] { 255.0/255, 117.0/255, 4.0/255, 1}),
	FIERY5("FIERY5", new double[] { 255.0/255, 69.0/255, 0.0/255, 1}),
	YELLOW("YELLOW", new double[] { 255.0/255, 255.0/255, 51.0/255, 1});

	/** The RGBA set contained in this EnumColor, in form of { r, g, b, a} */
	private final double[] COLOR;
	/** A plaintext description of this colour. */
	private final String name;
	/** A Vector of all the possible colours. */
	private static Vector<EnumColor> colors;
	static
	{
		colors = new Vector<EnumColor>();
		for (EnumColor tier: EnumSet.allOf(EnumColor.class))
        {
			colors.add(tier);
        }
	}
	
	/**
	 * Constructs a new EnumColor with given name and RGBA.
	 * @param name a plaintext representation of the colour
	 * @param colour the RGBA colour data of this colour in form { r, g, b, a }
	 */
	EnumColor(String name, double[] colour)
	{
		this.name = name;
		this.COLOR = colour;
	}
	
	/**
	 * Gets the EnumColor with a matching name as the name parameter provided. 
	 * @param name a String that matches the name of an EnumColor
	 * @return an EnumColor that shares the same name as the provided one; or EnumColor.WHITE if no matching colour is found
	 */
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
	
	/**
	 * Gets the RGBA colour data for this EnumColor in the form of a double[] { r, g, b, a } with values between 0 and 1.
	 * @return the RGBA colour data for this EnumColor
	 */
	public double[] getColors()
	{
		return COLOR;
	}
}
