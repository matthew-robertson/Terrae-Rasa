package net.dimensia.src;

public enum EnumDifficulty
{
	EASY(0.5f),
	NORMAL(1.0f),
	HARDCORE(1.5f);

	/**
	 * EASY -> DamageModifier of 0.5f; drop nothing;
	 * NORMAL -> DamageModifier of 1.0f; drop 1/2 coins;
	 * HARDCORE -> Delete Everything
	 */
	
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
	
	public float damageModifier;
}
