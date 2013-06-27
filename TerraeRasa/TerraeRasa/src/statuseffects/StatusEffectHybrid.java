package statuseffects;

import world.World;
import entities.EntityLiving;

/**
 * A HybridStatusEffect combines the effects of two or more statuseffects together into one. It will 
 * call apply(...) methods of all effects when being applied, and the remove(...) methods of all
 * the effects when being removed in addition to also applying the periodic effects.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0 
 * @since       1.0
 */
public class StatusEffectHybrid extends StatusEffect 
{
	private static final long serialVersionUID = 1L;
	/** The effects caused by this HybridEffect*/
	private StatusEffect[] effects;
	
	public StatusEffectHybrid(double durationSeconds, int tier, double power, int ticksBetweenEffect, StatusEffect[] effects) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 0;
		iconY = 0;
	}
	
	public void applyInitialEffect(World world, EntityLiving entity)
	{	
		for(StatusEffect effect : effects)
		{
			effect.applyInitialEffect(world, entity);
		}
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		for(StatusEffect effect : effects)
		{
			effect.removeInitialEffect(world, entity);
		}
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		for(StatusEffect effect : effects)
		{
			effect.applyPeriodicBonus(world, entity);
		}
		ticksLeft--;
	}
	
	/**
	 * Provides a string of all the different StatusEffects contained. This string is formatted such that one
	 * effect is on each line (in essence, effects are separated by a newline character)
	 */
	public String toString()
	{
		String result = "";
		for(StatusEffect effect : effects)
		{
			result += effect.toString() + '\n';
		}
		return result;
	}
}
