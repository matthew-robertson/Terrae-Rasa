package net.dimensia.src;

public class StatusEffectBleed extends StatusEffect 
{
	public StatusEffectBleed(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
		this.isBeneficialEffect = false;
		iconX = 14;
		iconY = 0;
	}

	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		if(ticksLeft % (40 / tier) == 0)
		{
			entity.damageEntity(world, 4, false, false);
		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Bleed";
	}
}
