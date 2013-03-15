package net.dimensia.src;

public class StatusEffectDazed extends StatusEffect
{
	public StatusEffectDazed(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
		this.isBeneficialEffect = false;
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.movementSpeedModifier *= 0.4f + (tier * 0.1f);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.movementSpeedModifier /= 0.4f + (tier * 0.1f);
	}
	
	public String toString()
	{
		return "Status_Effect_Stun_Dazed";
	}
}
