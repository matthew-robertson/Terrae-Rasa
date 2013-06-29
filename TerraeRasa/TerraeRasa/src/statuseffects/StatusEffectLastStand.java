package statuseffects;

import utils.Damage;
import world.World;
import entities.EntityLiving;
import entities.EntityPlayer;
import enums.EnumDamageSource;
import enums.EnumDamageType;


/**
 * StatusEffectLastStand grants a player a given percentage of their maximum health, and increases maximum
 * health by that same amount until expiring. This effect can actually kill the player upon expiring if they 
 * do not have very much health left. A power value of 1.0 grants 100% more health; a value of 0.3 grants 30%
 * more health, etcetera. <b> This is specifically designed for a player. This will not affect an entityliving</b>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectLastStand extends StatusEffect
{
	private int actualHealthIncrease;
	
	public StatusEffectLastStand(double durationSeconds, int tier, double power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
	}

	public void applyInitialEffect(World world, EntityLiving entity)
	{
		if(entity instanceof EntityPlayer)
		{
			this.actualHealthIncrease = (int) (entity.maxHealth * power);
			((EntityPlayer) entity).temporaryMaxHealth += actualHealthIncrease;
			entity.heal(world, actualHealthIncrease, false);
			((EntityPlayer) entity).recalculateStats();
		}
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		if(entity instanceof EntityPlayer)
		{
			((EntityPlayer) entity).temporaryMaxHealth -= actualHealthIncrease;
			((EntityPlayer) entity).recalculateStats();
			entity.damage(world, 
					new Damage(actualHealthIncrease, 
							new EnumDamageType[] { EnumDamageType.NONE },
							EnumDamageSource.STATUS_EFFECT)
							.setCausesCombatStatus(false)
							.setIsDodgeable(false)
							.setPenetratesArmor(true)
							.setPiercesAbsorbs(true),
					false);
		}
	}
	
	public String toString()
	{
		return "Grants " + actualHealthIncrease + " health until expiring";
	}
}
