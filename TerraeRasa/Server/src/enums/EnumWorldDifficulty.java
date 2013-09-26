package enums;

import java.util.EnumSet;
import java.util.Vector;

/**
 * Describes the world difficulty settings. Version 1.0 includes the following difficulties: <br>
 * <ol>
 *  <li> Easy - Damage modifier of 0.75, reducing damage taken by the player(s) by 25%. </li>
 *  <li> Normal - Damage modifier of 1, causing no additional effects. </li>
 *  <li> Hard - Damage modifier of 1.5, increasing damage taken by the player(s) by 50%. </li>
 *  <li> Insane - Damage modifier of 2.25, increasing damage taken by the player(s) by 125%. </li> 
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

	/** The damage taken modifier of the given world difficulty. */
	private double damageModifier;
	/** The name of this world difficulty. This is a displayable text representation of the enum. */
	private String name;
	/** A collection of the different world difficulty names. */
	private static Vector<String> enumValues;
	static
	{
		//Puts all the different world difficulty names in a vector when the class is initialized.
		enumValues = new Vector<String>();
		for (EnumWorldDifficulty tier: EnumSet.allOf(EnumWorldDifficulty.class))
        {
			enumValues.add(tier.getName());
        }
	}
	
	/**
	 * Constructs a new EnumWorldDifficulty with given name and damageModifier.
	 * @param name the name of this EnumWorldDifficulty which will be displayed and used in I/O
	 * @param damageMod the modifier by which to apply to t
	 */
	EnumWorldDifficulty(String name, double damageMod)
	{
		damageModifier = damageMod;
		this.name = name;
	}
	
	/**
	 * Gets the damage modifier for this world difficulty. This is the value that damage done to players
	 * in that world is multipled by. Ex a modifier of 1.5 will increase damage taken by 50% as
	 * (damage_taken * damage_modifier) is the calculation used.
	 * @return the damage modifier of this world difficulty
	 */
	public double getDamageModifier()
	{
		return damageModifier;
	}
	
	/**
	 * Gets an appropriate difficulty setting for the given string. This will be EnumWorldDifficulty.NORMAL if none is found.
	 * @param difficultyName an appropriate String representing the difficulty setting to get
	 * @return an appropriate EnumWorldDifficulty for the given String; or EnumWorldDifficulty.NORMAL if none is found
	 */
	public static EnumWorldDifficulty getDifficulty(String difficultyName)
	{
		difficultyName = difficultyName.toLowerCase();
		if(difficultyName.equals("easy"))
		{
			return EnumWorldDifficulty.EASY;
		}
		if(difficultyName.equals("normal"))
		{
			return EnumWorldDifficulty.NORMAL;
		}
		if(difficultyName.equals("hard"))
		{
			return EnumWorldDifficulty.HARD;
		}
		if(difficultyName.endsWith("insane"))
		{
			return EnumWorldDifficulty.INSANE;
		}
		return EnumWorldDifficulty.NORMAL;
	}
	
	/**
	 * Gets a String[] representation of the enumValues Vector, which stores all the names of the different world difficulties
	 * when they are created at runtime.
	 * @return a String[] containing the different world difficulty names
	 */
	public static String[] getAllEnumAsStringArray()
	{
		String[] temp = new String[enumValues.size()];
		enumValues.copyInto(temp);
		return temp;
	}
	
	/**
	 * Gets the name of this world difficulty in a displayable String.
	 * @return the name of this world difficulty
	 */
	public String getName()
	{
		return name;
	}
}
