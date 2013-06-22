package statuseffects;

import entities.EntityLiving;

public class StatusEffectDamageBuff extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new damage buff effect. A damagebuff's strength is based on it's power, indicating the overall % of increase. 
	 * A power of 1 indicates a 100% increase, 0.5 a 50% increase, et cetera.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the damage buff
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffectDamageBuff(double durationSeconds, int tier, double power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 15;
		iconY = 2;
	}

	public void applyInitialEffect(EntityLiving entity)
	{
		entity.allDamageModifier *= 1 + power;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.allDamageModifier /= 1 + power;
	}
	
	public String toString()
	{
		return "Status_Effect_Damage_Buff";
	}
}
