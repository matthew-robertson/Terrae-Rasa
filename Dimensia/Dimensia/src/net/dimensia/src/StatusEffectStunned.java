package net.dimensia.src;

public class StatusEffectStunned extends StatusEffect
{
	public StatusEffectStunned(int durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.isStunned = true;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.isStunned = false;
	}
}
