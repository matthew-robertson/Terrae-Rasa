package net.dimensia.src;

public class StatusEffectStun extends StatusEffect
{
	public StatusEffectStun(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
		this.isBeneficialEffect = false;
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.isStunned = true;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.isStunned = false;
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		entity.isStunned = true;
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Stun";
	}
}
