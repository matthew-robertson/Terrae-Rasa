package statuseffects;

import entities.EntityLiving;
import world.World;

public class StatusEffectStun extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	public StatusEffectStun(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 11;
		iconY = 0;
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.setStunned(true);
	}
	
	public void removeInitialEffect(EntityLiving entity)
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
		return "Status_Effect_Stun";
	}
}