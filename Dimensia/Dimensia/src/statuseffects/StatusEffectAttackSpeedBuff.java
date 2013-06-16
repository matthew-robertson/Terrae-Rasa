package statuseffects;

import entities.EntityLiving;

public class StatusEffectAttackSpeedBuff extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new attack speed effect. An attack speed effect's strength is based on it's power, indicating the overall % of increase. 
	 * A power of 1 indicates a 100% increase, 0.5 a 50% increase, et cetera. <b>Attack speed is additive </b>
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the attack speed buff
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffectAttackSpeedBuff(double durationSeconds, int tier, double power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 14;
		iconY = 1;
	}
	
	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.attackSpeedModifier += power;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{
		entity.attackSpeedModifier -= power;
	}
	
	public String toString()
	{
		return "Status_Effect_Attack_Speed";
	}
}
