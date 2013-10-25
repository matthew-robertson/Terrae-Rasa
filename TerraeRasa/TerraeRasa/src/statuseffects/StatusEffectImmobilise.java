package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectImmobilise makes it so an entity is not allowed to move until the effect expires. This is effectively the same as 
 * a 100% slow, except slows are not allowed to fully prevent movement, capping at 90% instead.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectImmobilise extends StatusEffect 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new StatusEffectImmobilize taking only an argument for the length as other factors 
	 * are fixed and not subject to change.
	 * @param durationSeconds the duration of this effect in seconds
	 */
	public StatusEffectImmobilise(double durationSeconds) 
	{
		super(durationSeconds, 1, 1, 1);
	}

	public void applyInitialEffect(World world, EntityLiving entity)
	{
		entity.setIsImmobile(true);
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.setIsImmobile(false);
	}

	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		ticksLeft--;
		entity.setIsImmobile(true);
	}
	
	public String toString()
	{
		return "You cannot move for " + (int)(ticksLeft / 20) + " seconds.";
	}
}
