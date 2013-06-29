package auras;

import utils.Damage;
import world.World;
import entities.EntityPlayer;
import enums.EnumDamageType;

/**
 * AuraDamageResistance provides a damage resistance to some sort of damage type. This resistance can
 * actually be negative, in addition to positive, depending on the given damageModifer. A damage modifier 
 * of 1 is normal, 0.5 is halved damage, 1.5 is 50% more damage taken, etcetera. Only 1 damage type can be
 * resisted per aura. Damage with multiple types will be resisted.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraDamageResistance extends Aura
{
	private EnumDamageType resistanceType;
	private double damageModifier;
	
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
}
