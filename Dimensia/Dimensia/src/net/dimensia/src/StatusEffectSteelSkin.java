package net.dimensia.src;

public class StatusEffectSteelSkin extends StatusEffect
{
	public StatusEffectSteelSkin(float durationSeconds, int tier)
	{
		super(durationSeconds, tier);
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.defense *= 1 + (tier * 0.1f);
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.defense /= 1 + (tier * 0.1f);
	}
	
	public String toString()
	{
		return "Status_Effect_Steel_Skin";
	}
}
