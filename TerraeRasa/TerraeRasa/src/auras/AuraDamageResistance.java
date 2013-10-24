package auras;

import server.entities.EntityPlayer;
import utils.Damage;
import world.World;
import enums.EnumDamageType;

/**
 * AuraDamageResistance provides a damage resistance to some sort of damage type. This resistance can
 * actually be negative, in addition to positive, depending on the given damageModifer. A damage modifier 
 * of 1 is normal, 0.5 is halved damage, 1.5 is 50% more damage taken, et cetera. Only 1 damage type can be
 * resisted per aura. Damage with multiple types will be resisted.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraDamageResistance extends Aura
{
	private static final long serialVersionUID = 1L;
	/** The damage element to modify the resistance of*/
	private EnumDamageType resistanceType;
	/** The damage modifier for this given element, where 0 <= x < 1 will decrease damage taken and x >= 1 will increase damage taken.*/
	private double damageModifier;
	
	/**
	 * Constructs a new AuraDamageResistance.
	 * @param type the damage type to modify the resistance of
	 * @param damageModifier the damage modifier for this given element, where 0 <= x < 1 will decrease damage taken and x >= 1 will increase damage taken.
	 */
	public AuraDamageResistance(EnumDamageType type, double damageModifier)
	{
		super();
		this.resistanceType = type;
		this.damageModifier = damageModifier;
	}
	
	public void onDamageTaken(World world, EntityPlayer player, Damage damage)
	{
		for(EnumDamageType type : damage.getType())
		{
			if(this.resistanceType == type)
			{
				damage.multiplyDamageValue(damageModifier);
				return;
			}
		}		
	}	
	
	public String toString()
	{
		if(damageModifier < 1)
		{
			return "Reduces damage taken by " + resistanceType.toString() + " by " + (int)(100 * (1 - damageModifier)) + "%";
		}
		else
		{
			return "Increases damage taken by " + resistanceType.toString() + " by " + (int)(100 * (damageModifier - 1)) + "%";
		}
	}
}
