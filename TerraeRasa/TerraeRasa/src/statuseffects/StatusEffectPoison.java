package statuseffects;

import entities.EntityLiving;
import enums.EnumDamageSource;
import enums.EnumDamageType;
import utils.Damage;
import world.World;

public class StatusEffectPoison extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	public StatusEffectPoison(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 15;
		iconY = 0;
	}

	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		if(ticksLeft % ticksBetweenEffect == 0)
		{
			entity.damage(world, 
					new Damage(power, 
							new EnumDamageType[] { EnumDamageType.POISON }, 
							EnumDamageSource.STATUS_EFFECT).setIsDodgeable(false).
							setCausesCombatStatus(false).setIsPeriodic(true), 
					false);

		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Poison";
	}
}
