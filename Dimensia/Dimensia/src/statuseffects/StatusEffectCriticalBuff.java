package statuseffects;

import entities.EntityLiving;

public class StatusEffectCriticalBuff extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a critical strike buff effect. A critical buff's strength is based on it's power, indicating the overall % of increase. 
	 * A power of 1 indicates a 100% increase, 0.5 a 50% increase, et cetera. A critical chance of 100% in general means everything
	 * will critically hit <b> critical strike is additive </b>
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the damage buff
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffectCriticalBuff(float durationSeconds, int tier, float power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 12; 
		iconY = 1;
	}
	
	public void applyInitialEffect(EntityLiving entity)
	{
		entity.criticalStrikeChance += power;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.criticalStrikeChance -= power;
	}
	
	public String toString()
	{
		return "Status_Effect_Critical";
	}
}
