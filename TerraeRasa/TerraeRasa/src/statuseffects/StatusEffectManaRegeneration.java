package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectManaRegeneration causes an entity to regenerate a given amount of mana every given number of seconds (measured
 * in game ticks). 1 point of power is 1 point of regenerated mana. Unlike health regeneration, mana regeneration cannot be 
 * percentage based.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectManaRegeneration extends StatusEffect
{

	public StatusEffectManaRegeneration(double durationSeconds, int tier, double power, int ticksBetweenEffect)
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 13;
		iconY = 2;
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		if(ticksLeft % ticksBetweenEffect == 0)
		{
			entity.restoreMana(world, (int)power, false);
		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Mana_Regeneration";
	}
}
