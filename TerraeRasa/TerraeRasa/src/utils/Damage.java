package utils;

import enums.EnumDamageSource;
import enums.EnumDamageType;

/**
 * Damage is a container class for a hit
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Damage 
{
	private int damageAmount;
	private EnumDamageType type;
	private EnumDamageSource source;
	
	public Damage(int damageAmount, EnumDamageType type, EnumDamageSource source)
	{
		
	}
	
	public int getDamageAmount()
	{
		return damageAmount;
	}
	
	public EnumDamageType getType()
	{
		return type;
	}
	
	public EnumDamageSource getSource()
	{
		return source;
	}
}
