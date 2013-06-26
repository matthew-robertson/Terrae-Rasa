package utils;

import enums.EnumDamageSource;
import enums.EnumDamageType;

/**
 * Damage is a container class for a hit of damage dealt to some entity. It contains information 
 * regarding the damage amount, types, source; as well as if it is dodgeable, penetrates absorbs,
 * is a critical hit, and if the damage will penetrate armour.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Damage 
{
	private int damageAmount;
	private EnumDamageType[] types;
	private EnumDamageSource source;
	private boolean isDodgeable;
	private boolean penetratesArmor;
	private boolean piercesAbsorbs;
	private boolean isCrit;
	
	/**
	 * Constructs a new damage object. By default this will not penetrate armour, or critically hit
	 * but is dodgeable.
	 * @param damageAmount the amount of damage to deal
	 * @param types the types of the damage dealt (this only needs to be 1 element)
	 * @param source the source of the damage, such as melee or the environment
	 */
	public Damage(int damageAmount, EnumDamageType[] types, EnumDamageSource source)
	{
		this.damageAmount = damageAmount;
		this.types = types;
		this.source = source;
		isDodgeable = true;
		penetratesArmor = false;
		piercesAbsorbs = false;
		isCrit = false;
	}
	
	public Damage setIsDodgeable(boolean flag)
	{
		this.isDodgeable = flag;
		return this;
	}
	
	public Damage setPenetratesArmor(boolean flag)
	{
		this.penetratesArmor = flag;
		return this;		
	}
	
	public Damage setPiercesAbsorbs(boolean flag)
	{
		this.piercesAbsorbs = flag;
		return this;
	}
	
	public Damage setIsCrit(boolean flag)
	{
		this.isCrit = flag;
		return this;
	}

	public int getDamageAmount()
	{
		return damageAmount;
	}
	
	public EnumDamageType[] getType()
	{
		return types;
	}
	
	public EnumDamageSource getSource()
	{
		return source;
	}
	
	public boolean getIsCrit()
	{
		return isCrit;
	}
	
	public boolean getIsDodgeable()
	{
		return isDodgeable;
	}
	
	public boolean getPiercesAbsorbs()
	{
		return piercesAbsorbs;
	}
	
	public boolean getPenetratesArmor()
	{
		return penetratesArmor;
	}
}
