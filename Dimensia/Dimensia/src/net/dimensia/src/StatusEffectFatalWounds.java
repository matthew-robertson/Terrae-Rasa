package net.dimensia.src;

public class StatusEffectFatalWounds extends StatusEffect
{
	public StatusEffectFatalWounds(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
		reapplicationSkipsRemovalEffect = true;
		this.isBeneficialEffect = false;
		iconX = 6;
		iconY = 2;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{
		entity.health -= entity.maxHealth - 1;
	}
	
	public String toString()
	{
		return "Status_Effect_Fatal_Wounds";
	}
}
