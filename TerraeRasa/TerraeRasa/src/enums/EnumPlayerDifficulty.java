package enums;

import java.util.EnumSet;
import java.util.Vector;


/**
 * EnumPlayerDifficulty defines the different player difficulties. In Version 1.0 of this enum there are 3:
 * <ol>
 * <li> Normal - No effect. </li>
 * <li> Hard - Drop all items on death. </li>
 * <li> Hardcore - The player is erased. </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumPlayerDifficulty 
{
	/** No Effect*/
	NORMAL("Normal"),
	/** Drops all items on death */
	HARD("Hard"),
	/** Character deleted on death*/
	HARDCORE("Hardcore");	

	private String name;
	private static Vector<String> enumValues;
	static
	{
		//Add all the EnumArmor to the armorTiers Vector to automatically update the SetBonusFactory and other classes
		//of a change to EnumArmor
		enumValues = new Vector<String>();
		for (EnumPlayerDifficulty tier: EnumSet.allOf(EnumPlayerDifficulty.class))
        {
			enumValues.add(tier.getName());
        }
	}
	
	EnumPlayerDifficulty(String name)
	{
		this.name = name;
	}
	
	/**
	 * Gets a player difficulty enum based on a string value.
	 * @param s the difficulty mode as represented by a string
	 * @return an appropriate difficulty mode; or EnumPlayerDifficulty.NORMAL if none is found
	 */
	public static EnumPlayerDifficulty getDifficulty(String s)
	{
		s = s.toLowerCase();
		if(s.equals("normal"))
		{
			return EnumPlayerDifficulty.NORMAL;
		}
		if(s.equals("hard"))
		{
			return EnumPlayerDifficulty.HARD;
		}
		if(s.equals("hardcore"))
		{
			return EnumPlayerDifficulty.HARDCORE;
		}
		return EnumPlayerDifficulty.NORMAL;
	}
	
	public String getName()
	{
		return name;
	}
	
	public static String[] getAllEnumAsStringArray()
	{
		String[] temp = new String[enumValues.size()];
		enumValues.copyInto(temp);
		return temp;
	}
}
