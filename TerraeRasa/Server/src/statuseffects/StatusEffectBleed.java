package statuseffects;

import entities.EntityLiving;
import enums.EnumDamageSource;
import enums.EnumDamageType;
import utils.Damage;
import world.World;

/**
 * StatusEffectBleed deals periodic damage to an entity. This damage is based on the power value, where 1 power is 1 damage 
 * dealt every interval. StatusEffectBleed cannot be percentile based damage, is not able to be dodged, and will 
 * not cause combat status.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectBleed extends StatusEffect 
{

	public StatusEffectBleed(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 14;
		iconY = 0;
	}

	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		if(ticksLeft % ticksBetweenEffect == 0)
		{
			entity.damage(world, 
					new Damage(power, 
							new EnumDamageType[] { EnumDamageType.BLEED }, 
							EnumDamageSource.STATUS_EFFECT).setIsDodgeable(false).
							setCausesCombatStatus(false).setIsPeriodic(true), 
					false);
		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Bleed";
	}
}
