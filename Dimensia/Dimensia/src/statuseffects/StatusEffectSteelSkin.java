package statuseffects;

import entities.EntityLiving;

public class StatusEffectSteelSkin extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	public StatusEffectSteelSkin(float durationSeconds, int tier, float power, int ticksBetweenEffect)
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 15;
		iconY = 1;
	}

	public void applyInitialEffect(EntityLiving entity)
	{	
		entity.defense += power;
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
		entity.defense -= power;
	}
	
	public String toString()
	{
		return "Status_Effect_Steel_Skin";
	}
}
