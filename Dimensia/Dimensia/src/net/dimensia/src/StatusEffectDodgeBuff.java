package net.dimensia.src;

public class StatusEffectDodgeBuff extends StatusEffect
{
	public StatusEffectDodgeBuff(float durationSeconds, int tier)
	{
		super(durationSeconds, tier);
	}
	
	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.dodgeChance *= 1 + (0.1f * tier);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.dodgeChance /= 1 + (0.1f * tier);
	}
	
	public String toString()
	{
		return "Status_Effect_Dodge";
	}
}
