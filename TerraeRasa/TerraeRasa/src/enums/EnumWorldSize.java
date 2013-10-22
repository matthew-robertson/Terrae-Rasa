package enums;

import java.util.EnumSet;
import java.util.Vector;

/**
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumWorldSize
{
	MINI("Mini", 1200, 1200),
	SMALL("Small", 2400, 1200), 
	MEDIUM("Medium", 3600, 1200), 
	LARGE("Large", 4800, 1200); 

	/** The width of this world size in blocks. */
	private int width;
	/** The height of this world size in blocks. */
	private int height;
	/** The name of this world size in plaintext format, and can be displayed. */
	private String name;
	/** A collection of all the world size names. */
	private static Vector<String> enumValues;
	static
	{
		//Put all the world size names into the enumValues Vector
		enumValues = new Vector<String>();
		for (EnumWorldSize tier: EnumSet.allOf(EnumWorldSize.class))
        {
			enumValues.add(tier.getName());
        }
	}
	
	/**
	 * Constructs a new EnumWorldSize with the given parameters.
	 * @param name the name of the new world size, in plaintext
	 * @param w the width of the new world size in blocks
	 * @param h the height of the new world size in blocks
	 */
	EnumWorldSize(String name, int w, int h)
	{
		width = w;
		height = h;
		this.name = name;
	}
	
	/**
	 * The width of this world size in blocks, which will tell how wide the world is.
	 * @return the width of this world size in blocks
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * The height of this world size, which will be the number of blocks of depth in this world size.
	 * @return the height of this world size in blocks.
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Gets a world size by its name. This will default to MEDIUM size if nothing matches.
	 * @param s a String corresponding to a world size name
	 * @return the world size whose name matches the String provided, or MEDIUM world size if no matches are found
	 */
	public static EnumWorldSize getSize(String s)
	{
		s = s.toLowerCase();
		if(s.equals("mini"))
		{
			return EnumWorldSize.MINI;
		}
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
	
	/**
	 * Gets the names of all the world sizes stored in enumValues as a String[]
	 * @return a String[] containing all the different world size names
	 */
	public static String[] getAllEnumAsStringArray()
	{
		String[] temp = new String[enumValues.size()];
		enumValues.copyInto(temp);
		return temp;
	}
	
	/**
	 * Gets the name of this world size, a plaintext representation that can be displayed.
	 * @return the name of this world size in plain text
	 */
	public String getName()
	{
		return name;
	}
}
