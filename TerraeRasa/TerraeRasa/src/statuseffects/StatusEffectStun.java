package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectStun stuns the entity for the given period of time. Stun prevents attacking as well as movement.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectStun extends StatusEffect
{

	public StatusEffectStun(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 11;
		iconY = 0;
	}

	public void applyInitialEffect(World world, EntityLiving entity)
	{	
		entity.setStunned(true);
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.setStunned(false);
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		entity.setStunned(true);
		ticksLeft--;
	}
	
	public String toString()
	{
		return "You are stunned!";
	}
}
