package enums;

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
	EASY(0.75),
	/** Difficulty modifier of 1.0, causing no additional effect */
	NORMAL(1.0),
	/** Difficulty modifier of 1.5, increasing damage taken by the player(s) by 50% */
	HARD(1.5),
	/** Difficulty modifier of 2.25, increasing damage taken by the player(s) by 125% */
	INSANE(2.25);

	public double damageModifier;
	
	EnumWorldDifficulty(double f)
	{
		damageModifier = f;
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
}
