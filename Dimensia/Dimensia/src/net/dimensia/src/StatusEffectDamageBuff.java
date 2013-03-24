package net.dimensia.src;

public class StatusEffectDamageBuff extends StatusEffect
{
	public StatusEffectDamageBuff(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
		iconX = 15;
		iconY = 2;
	}

	public void applyInitialEffect(EntityLiving entity)
	{
		entity.allDamageModifier *= 1 + (tier * 0.1f);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.allDamageModifier /= 1 + (tier * 0.1f);
	}
	
	public String toString()
	{
		return "Status_Effect_Damage_Buff";
	}
}
