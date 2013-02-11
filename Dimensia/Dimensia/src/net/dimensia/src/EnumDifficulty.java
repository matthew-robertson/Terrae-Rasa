package net.dimensia.src;

/**
 * Describes the world difficulty settings. <br>
 * EASY -> DamageModifier of 0.5f; drop nothing; <br>
 * NORMAL -> DamageModifier of 1.0f; drop 1/2 coins; <br>
 * HARDCORE -> Delete Everything <br>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumDifficulty
{
	EASY(0.5f),
	NORMAL(1.0f),
	HARDCORE(1.5f);

	public float damageModifier;
	
	EnumDifficulty(float f)
	{
		damageModifier = f;
	}
	
	public float getDamageModifier()
	{
		return damageModifier;
	}
	
	public static EnumDifficulty getDifficulty(String s)
	{
		s = s.toLowerCase();
		if(s.equals("easy"))
		{
			return EnumDifficulty.EASY;
		}
		if(s.equals("normal"))
		{
			return EnumDifficulty.NORMAL;
		}
		if(s.equals("hardcore"))
		{
			return EnumDifficulty.HARDCORE;
		}
		return EnumDifficulty.NORMAL;
	}
}
