package statuseffects;

import world.World;
import entities.EntityLiving;

/**
 * StatusEffectSwiftness increases an entity's movement speed by a given amount. This amount is a multiplicative percentage 
 * based on the statuseffect's power where 0.0 is no increase and 1.0 is a 100% increase. Amounts beyond 1.0 increase 
 * beyond 100% linearly.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectSwiftness extends StatusEffect
{

	/**
	 * Creates a new damage speed effect. A speed effect's strength is based on it's power, indicating the overall % of increase. 
	 * A power of 1 indicates a 100% increase, 0.5 a 50% increase, et cetera.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the swiftness effect
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffectSwiftness(double durationSeconds, int tier, double power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 12;
		iconY = 0;
	}

	public void applyInitialEffect(World world, EntityLiving entity)
	{	
		entity.setMovementSpeedModifier(entity.getMovementSpeedModifier()
				* (1 + power));
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.setMovementSpeedModifier(entity.getMovementSpeedModifier()
				/ (1 + power));
	}
	
	public String toString()
	{
		return "Increases movement speed by " + (int)(100 * power) + "%";
	}
}
