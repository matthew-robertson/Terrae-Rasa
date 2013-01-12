package net.dimensia.src;

public class StatusEffectAttackSpeedBuff extends StatusEffect
{
	public StatusEffectAttackSpeedBuff(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
	}
	
	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.attackSpeedModifier *= 1 + (tier * 0.1f);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{
		entity.attackSpeedModifier /= 1 + (tier * 0.1f);
	}
	
	public String toString()
	{
		return "Status_Effect_Attack_Speed";
	}
}
