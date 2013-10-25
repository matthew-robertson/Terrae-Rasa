package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectSlowed decreases an entity's movement speed by a given amount. This amount is a multiplicative percentage 
 * based on the statuseffect's power where 0.0 is no effect and 1.0 is full effect. A daze is capped at 95% effectiveness
 * though to prevent complete snares (which would be another type of debuff)
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectSlowed extends StatusEffect
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
	public StatusEffectSlowed(double durationSeconds, int tier, double power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 13;
		iconY = 0;
		if(power >= 95F) 
			power = 0.95F;
	}

	public void applyInitialEffect(World world, EntityLiving entity)
	{	
		entity.setMovementSpeedModifier(entity.getMovementSpeedModifier()
				* (1F - power));
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.setMovementSpeedModifier(entity.getMovementSpeedModifier()
				/ (1F - power));
	}
	
	public String toString()
	{
		return "Reduces speed by " + (int)(power * 100) + "%";
	}
}
