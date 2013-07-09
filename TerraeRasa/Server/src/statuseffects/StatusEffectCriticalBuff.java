package statuseffects;

import world.World;
import entities.EntityLiving;

/**
 * StatusEffectCriticalBuff increases an entity's critical strike chance by a given amount. This amount is an additive percentage 
 * based on the statuseffect's power where 0.0 is no increase and 1.0 is a 100% increase. Amounts beyond 1.0 increase 
 * beyond 100% linearly, though 100% critical strike chance generally means a hit will be a critical strike.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectCriticalBuff extends StatusEffect
{

	/**
	 * Creates a critical strike buff effect. A critical buff's strength is based on it's power, indicating the overall % of increase. 
	 * A power of 1 indicates a 100% increase, 0.5 a 50% increase, et cetera. A critical chance of 100% in general means everything
	 * will critically hit <b> critical strike is additive </b>
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the damage buff
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffectCriticalBuff(double durationSeconds, int tier, double power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 12; 
		iconY = 1;
	}
	
	public void applyInitialEffect(World world, EntityLiving entity)
	{
		entity.criticalStrikeChance += power;
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.criticalStrikeChance -= power;
	}
	
	public String toString()
	{
		return "Increases crit by " + (int)(power * 100) + "%";
	}
}
