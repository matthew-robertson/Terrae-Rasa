package statuseffects;

import entities.EntityLiving;
import world.World;

/**
 * StatusEffectRegeneration causes an entity to regenerate a given amount of health every given number of seconds (measured
 * in game ticks). 1 point of power is 1 point of regenerated health if the regeneration is not percentage based. If
 * the regeneration is percentage based then a certain percentage of the player's health is restored based on the power value.
 * For percentage healing, a power value of 0.0 is no regenerated health and 1.0 is 100% of the entity's health regenerated.
 * This regeneration effect is applied every time the effect ticks regardless of being percentage based or not. 
 * <br> <br>
 * Ex. power of 0.05 and 2 seconds between effects would restore 5 health every 2 seconds if the entity had 100 HP. This would last
 * for however long the effect was active.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectRegeneration extends StatusEffect
{
	private boolean percentageBased;
	
	/**
	 * Creates a new StatusEffectRegeneration which will restore a given amount or 
	 * percent of health every given number of game ticks.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the bonus, used for stacking rules
	 * @param power the strength of the effect - in percent or flat healing
	 * @param ticksBetweenEffect the number of game ticks between heals
	 * @param percentageBased true if the effect heals a % of player health; false if it heals a flat amount
	 */
	public StatusEffectRegeneration(double durationSeconds, int tier, double power, int ticksBetweenEffect, boolean percentageBased) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 14;
		iconY = 2;
		this.percentageBased = percentageBased;
	}

	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		if(ticksLeft % ticksBetweenEffect == 0)
		{
			if(percentageBased)
			{
				entity.heal(world, ((power / 100) * entity.maxHealth), false);
			}
			else
			{
				entity.heal(world, power, false);
			}
		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Regenerates " + (int)power + " health every " + ((double)(ticksBetweenEffect) / 20) + " seconds";
	}
}
