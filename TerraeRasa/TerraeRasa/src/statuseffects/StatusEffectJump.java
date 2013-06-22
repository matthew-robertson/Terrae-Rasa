package statuseffects;

import entities.EntityLiving;

public class StatusEffectJump extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	public StatusEffectJump(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 0;
		iconY = 0;
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.setUpwardJumpHeight(entity.getUpwardJumpHeight() + power);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.setUpwardJumpHeight(entity.getUpwardJumpHeight() - power);
	}
	
	public String toString()
	{
		return "Status_Effect_Jump_Height";
	}
}
