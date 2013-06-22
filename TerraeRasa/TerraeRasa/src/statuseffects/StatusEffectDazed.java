package statuseffects;

import entities.EntityLiving;

public class StatusEffectDazed extends StatusEffect
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new daze effect. A daze effect's strength is based on it's power from 0-1F, indicating the overall % slowed. 
	 * A value of 0.6 for example indicates an overall slow of 60% (multiplicative). A daze is capped at 95% effectiveness.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the daze
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffectDazed(double durationSeconds, int tier, double power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 13;
		iconY = 0;
		if(power >= 95F) 
			power = 0.95F;
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.setMovementSpeedModifier(entity.getMovementSpeedModifier()
				* (1F - power));
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.setMovementSpeedModifier(entity.getMovementSpeedModifier()
				/ (1F - power));
	}
	
	public String toString()
	{
		return "Status_Effect_Stun_Dazed";
	}
}
