package enums;

import java.util.EnumSet;
import java.util.Vector;


/**
 * EnumPlayerDifficulty defines the different player difficulties. In Version 1.0 of this enum there are 3:
 * <ol>
 *  <li> Normal - No effect. </li>
 *  <li> Hard - Drop all items on death. </li>
 *  <li> Hardcore - The player is erased. </li>
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

	/** A displayable value representing this player difficulty mode. */
	private String name;
	/** A Vector containing the names of all the player difficulties, constructed at runtime.*/
	private static Vector<String> enumValues;
	static
	{
		//Add all the EnumPlayerDifficulty settings to the enumValues Vector
		enumValues = new Vector<String>();
		for (EnumPlayerDifficulty tier: EnumSet.allOf(EnumPlayerDifficulty.class))
        {
			enumValues.add(tier.getName());
        }
	}
	
	/**
	 * Constructs a new EnumPlayerDifficulty with given name.
	 * @param name the displayable name of this player difficulty.
	 */
	EnumPlayerDifficulty(String name)
	{
		this.name = name;
	}
	
	/**
	 * Gets a player difficulty enum based on a string value which corresponds to its given name.
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
	
	/**
	 * Gets this player difficulty as a displayable String of text.
	 * @return this player difficulty as a String
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets a String[] of all the different player difficulty names stored in the enumValues Vector,
	 * which was populated at Runtime.
	 * @return all the names of the different player difficulties
	 */
	public static String[] getAllEnumAsStringArray()
	{
		String[] temp = new String[enumValues.size()];
		enumValues.copyInto(temp);
		return temp;
	}
}
