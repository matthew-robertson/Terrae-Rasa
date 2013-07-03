package enums;

import java.util.EnumSet;
import java.util.Vector;

public enum EnumWorldSize
{
	MINI("Mini", 1200, 800),
	SMALL("Small", 2400, 1200), 
	MEDIUM("Medium", 3600, 1800), 
	LARGE("Large", 4800, 2400); 

	private int width;
	private int height;
	private String name;
	private static Vector<String> enumValues;
	static
	{
		//Add all the EnumArmor to the armorTiers Vector to automatically update the PassiveBonusFactory and other classes
		//of a change to EnumArmor
		enumValues = new Vector<String>();
		for (EnumWorldSize tier: EnumSet.allOf(EnumWorldSize.class))
        {
			enumValues.add(tier.getName());
        }
	}
	
	EnumWorldSize(String name, int w, int h)
	{
		width = w;
		height = h;
		this.name = name;
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
	
	public static String[] getAllEnumAsStringArray()
	{
		String[] temp = new String[enumValues.size()];
		enumValues.copyInto(temp);
		return temp;
	}
	
	public String getName()
	{
		return name;
	}
}
