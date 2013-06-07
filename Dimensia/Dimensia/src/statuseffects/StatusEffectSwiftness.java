package statuseffects;

import entities.EntityLiving;

public class StatusEffectSwiftness extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new damage speed effect. A speed effect's strength is based on it's power, indicating the overall % of increase. 
	 * A power of 1 indicates a 100% increase, 0.5 a 50% increase, et cetera.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the swiftness effect
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffectSwiftness(float durationSeconds, int tier, float power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 12;
		iconY = 0;
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.movementSpeedModifier *= 1 + power;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.movementSpeedModifier /= 1 + power;
	}
	
	public String toString()
	{
		return "Status_Effect_Swiftness";
	}
}
