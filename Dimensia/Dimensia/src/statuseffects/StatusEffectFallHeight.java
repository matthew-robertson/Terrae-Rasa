package statuseffects;

import entities.EntityLiving;

public class StatusEffectFallHeight extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	public StatusEffectFallHeight(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 0;
		iconY = 0;
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.setMaxHeightFallenSafely(entity.getMaxHeightFallenSafely() + power);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.setMaxHeightFallenSafely(entity.getMaxHeightFallenSafely() - power);
	}
	
	public String toString()
	{
		return "Status_Effect_Fall_Height";
	}
}
