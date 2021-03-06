package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectDodgeBuff increases an entity's dodge chance by a given amount. This amount is an additive percentage 
 * based on the statuseffect's power where 0.0 is no increase and 1.0 is a 100% increase. Amounts beyond 1.0 increase 
 * beyond 100% linearly. 100% dodge is probably unhittable, though certain damage hits cannot be dodged.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
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
	public StatusEffectDodgeBuff(double durationSeconds, int tier, double power, int ticksBetweenEffect)
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 11;
		iconY = 1;
	}
	
	public void applyInitialEffect(World world, EntityLiving entity)
	{	
		entity.dodgeChance += power;
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.dodgeChance -= power;
	}
	
	public String toString()
	{
		return "Increases dodge chance by " + (int)(power * 100) + "%";
	}
}
