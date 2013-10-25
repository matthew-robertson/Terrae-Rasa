package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectDamageBuff increases an entity's damage by a given amount. This amount is a multiplicative percentage 
 * based on the statuseffect's power where 0.0 is no increase and 1.0 is a 100% increase. Amounts beyond 1.0 increase 
 * beyond 100% linearly.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
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

	public void applyInitialEffect(World world, EntityLiving entity)
	{
		entity.allDamageModifier *= 1 + power;
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.allDamageModifier /= 1 + power;
	}
	
	public String toString()
	{
		return "Increases damage by " + (int)(power * 100) + "%";
	}
}
