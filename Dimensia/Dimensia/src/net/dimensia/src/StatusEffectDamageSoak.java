package net.dimensia.src;

public class StatusEffectDamageSoak extends StatusEffect
{
	public StatusEffectDamageSoak(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
	}
	
	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.damageSoakModifier *= 1 + (tier * 0.1f);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{
		entity.damageSoakModifier /= 1 + (tier * 0.1f);
	}
	
	public String toString()
	{
		return "Status_Effect_Damage_Soak";
	}
}
