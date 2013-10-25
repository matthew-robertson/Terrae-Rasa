package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectAttackSpeed increases an entity's attack speed by a given amount. This amount is an additive percentage 
 * based on the statuseffect's power where 0.0 is no increase and 1.0 is a 100% increase. Amounts beyond 1.0 increase 
 * beyond 100% linearly. A 100% increase does not mean instant attacks, and may not provide a monster with any 
 * effect at all. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
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
	
	public void applyInitialEffect(World world, EntityLiving entity)
	{	
		entity.attackSpeedModifier += power;
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{
		entity.attackSpeedModifier -= power;
	}
	
	public String toString()
	{
		return "Increases attack speed by " + (int)(power * 100) + "%";
	}
}
