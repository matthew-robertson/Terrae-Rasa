package enums;

import java.util.EnumSet;
import java.util.Vector;

/**
 * Describes the world difficulty settings. Version 1.0 includes the following difficulties: <br>
 * <ol>
 *  <li> Easy - Damage modifier of 0.75</li>
 *  <li> Normal - Damage modifier of 1</li>
 *  <li> Hard - Damage modifier of 1.5</li>
 *  <li> Insane - Damage modifier of 2.25 </li> 
 * </ol>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumWorldDifficulty
{
	/** Difficulty modifier of 0.75, reducing damage taken by the player(s) by 25% */
	EASY("Easy", 0.75),
	/** Difficulty modifier of 1.0, causing no additional effect */
	NORMAL("Normal", 1.0),
	/** Difficulty modifier of 1.5, increasing damage taken by the player(s) by 50% */
	HARD("Hard", 1.5),
	/** Difficulty modifier of 2.25, increasing damage taken by the player(s) by 125% */
	INSANE("Insane", 2.25);

	public double damageModifier;
	private String name;
	private static Vector<String> enumValues;
	static
	{
		//Add all the EnumArmor to the armorTiers Vector to automatically update the SetBonusFactory and other classes
		//of a change to EnumArmor
		enumValues = new Vector<String>();
		for (EnumWorldDifficulty tier: EnumSet.allOf(EnumWorldDifficulty.class))
        {
			enumValues.add(tier.getName());
        }
	}
	
	EnumWorldDifficulty(String name, double f)
	{
		damageModifier = f;
		this.name = name;
	}
	
	public double getDamageModifier()
	{
		return damageModifier;
	}
	
	/**
	 * Gets an appropriate difficulty setting for the given string. This will be EnumWorldDifficulty.NORMAL if none is found.
	 * @param s an appropriate String representing the difficulty setting to get
	 * @return an appropriate EnumWorldDifficulty for the given String; or EnumWorldDifficulty.NORMAL if none is found
	 */
	public static EnumWorldDifficulty getDifficulty(String s)
	{
		s = s.toLowerCase();
		if(s.equals("easy"))
		{
			return EnumWorldDifficulty.EASY;
		}
		if(s.equals("normal"))
		{
			return EnumWorldDifficulty.NORMAL;
		}
		if(s.equals("hard"))
		{
			return EnumWorldDifficulty.HARD;
		}
		if(s.endsWith("insane"))
		{
			return EnumWorldDifficulty.INSANE;
		}
		return EnumWorldDifficulty.NORMAL;
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
