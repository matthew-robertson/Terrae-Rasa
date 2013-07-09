package utils;

import enums.EnumDamageSource;
import enums.EnumDamageType;

/**
 * Damage is a container class for a hit of damage dealt to some entity. It contains information 
 * regarding the damage amount, types, source; as well as if it is dodgeable, penetrates absorbs,
 * is a critical hit, and if the damage will penetrate armour. Damage can also be flagged as 
 * periodic, and will cause combat_status unless otherwise stated.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Damage 
{
	private double damageAmount;
	private EnumDamageType[] types;
	private EnumDamageSource source;
	private boolean isDodgeable;
	private boolean penetratesArmor;
	private boolean piercesAbsorbs;
	private boolean isCrit;
	private boolean isPeriodic;
	private boolean causesCombatStatus;
		
	/**
	 * Constructs a new damage object. By default this will not penetrate armour, or critically hit
	 * but is dodgeable. Additionally, the damage is not considered periodic but will inflict combat
	 * status.
	 * @param damageAmount the amount of damage to deal
	 * @param types the types of the damage dealt (this only needs to be 1 element)
	 * @param source the source of the damage, such as melee or the environment
	 */
	public Damage(double damageAmount, EnumDamageType[] types, EnumDamageSource source)
	{
		this.damageAmount = damageAmount;
		this.types = types;
		this.source = source;
		isDodgeable = true;
		penetratesArmor = false;
		piercesAbsorbs = false;
		isCrit = false;
		isPeriodic = false;
		causesCombatStatus = true;
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

	public double amount()
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
	
	public boolean isCrit()
	{
		return isCrit;
	}
	
	public boolean isDodgeable()
	{
		return isDodgeable;
	}
	
	public boolean piercesAbsorbs()
	{
		return piercesAbsorbs;
	}
	
	public boolean penetratesArmor()
	{
		return penetratesArmor;
	}
	
	public Damage setCausesCombatStatus(boolean flag)
	{
		this.causesCombatStatus = flag;
		return this;
	}
	
	public Damage setIsPeriodic(boolean flag)
	{
		this.isPeriodic = flag;
		return this;
	}
	
	/**
	 * Multiples the damage value of this damage object by a given multiplier. Cannot reduce damage below 1.
	 * @param multiplier the multipler to apply to this damage object
	 */
	public void multiplyDamageValue(double multiplier)
	{
		this.damageAmount *= multiplier;
		if(damageAmount < 1)
		{
			damageAmount = 1;
		}
	}
	
	/**
	 * Adds a given amount of damage to this damage object.
	 * @param amount the damage to add to this damage object
	 */
	public void addDamage(double amount)
	{
		this.damageAmount += amount;
	}
	
	/**
	 * Removes a given amount of damage from this damage object. Cannot reduce damage below 1.
	 * @param amount the damage to remove from this damage object
	 */
	public void removeDamage(double amount)
	{
		this.damageAmount -= amount;
		if(damageAmount < 1)
		{
			damageAmount = 1;
		}
	}

	public boolean isPeriodic()
	{
		return isPeriodic;
	}
	
	public boolean causesCombatStatus()
	{
		return causesCombatStatus;
	}
}
