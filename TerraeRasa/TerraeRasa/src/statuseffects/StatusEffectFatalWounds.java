package statuseffects;

import entities.EntityLiving;

public class StatusEffectFatalWounds extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	public StatusEffectFatalWounds(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
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
