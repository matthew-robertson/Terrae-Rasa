package net.dimensia.src;

public class StatusEffectFatalWounds extends StatusEffect
{
	public StatusEffectFatalWounds(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
		reapplicationSkipsRemovalEffect = true;
		this.isBeneficialEffect = false;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{
		entity.health = -2000;
	}
	
	public String toString()
	{
		return "Status_Effect_Fatal_Wounds";
	}
}
