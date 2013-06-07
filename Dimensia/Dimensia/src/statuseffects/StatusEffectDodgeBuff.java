package statuseffects;

import entities.EntityLiving;

public class StatusEffectDodgeBuff extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new dodge effect. A dodge effect's strength is based on it's power, indicating the overall % of increase. 
	 * A power of 1 indicates a 100% increase, 0.5 a 50% increase, et cetera. 100% is probably "unhittable". <b>Dodge
	 * Stacks additively</b>
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the dodge buff
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffectDodgeBuff(float durationSeconds, int tier, float power, int ticksBetweenEffect)
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 11;
		iconY = 1;
	}
	
	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.dodgeChance += power;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.dodgeChance -= power;
	}
	
	public String toString()
	{
		return "Status_Effect_Dodge";
	}
}
