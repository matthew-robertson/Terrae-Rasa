package net.dimensia.src;

public class StatusEffectCriticalBuff extends StatusEffect
{
	public StatusEffectCriticalBuff(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
	}
	
	public void applyInitialEffect(EntityLiving entity)
	{
		entity.criticalStrikeChance *= 1 + (tier * 0.1f);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.criticalStrikeChance /= 1 + (tier * 0.1f);
	}
	
	public String toString()
	{
		return "Status_Effect_Critical";
	}
}
