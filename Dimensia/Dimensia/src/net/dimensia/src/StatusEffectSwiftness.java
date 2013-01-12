package net.dimensia.src;

public class StatusEffectSwiftness extends StatusEffect
{
	public StatusEffectSwiftness(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.movementSpeedModifier *= 1.2f + (tier * 0.1f);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.movementSpeedModifier /= 1.2f + (tier * 0.1f);
	}
	
	public String toString()
	{
		return "Status_Effect_Swiftness";
	}
}
