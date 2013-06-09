package statuseffects;

import entities.EntityLiving;
import world.World;

public class StatusEffectRegeneration extends StatusEffect
{
	private static final long serialVersionUID = 1L;
	private boolean percentageBased;
	
	/**
	 * 
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the bonus, used for stacking rules
	 * @param power the strength of the effect - in percent or flat healing
	 * @param ticksBetweenEffect the number of game ticks between heals
	 * @param percentageBased true if the effect heals a % of player health; false if it heals a flat amount
	 */
	public StatusEffectRegeneration(float durationSeconds, int tier, float power, int ticksBetweenEffect, boolean percentageBased) 
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
				entity.heal(world, (int)((power / 100F) * entity.maxHealth), false);
			}
			else
			{
				entity.heal(world, (int)power, false);
			}
		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Regeneration";
	}
}
