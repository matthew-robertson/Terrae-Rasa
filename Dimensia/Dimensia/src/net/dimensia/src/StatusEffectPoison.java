package net.dimensia.src;

public class StatusEffectPoison extends StatusEffect
{
	public StatusEffectPoison(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
		this.isBeneficialEffect = false;
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
		return "Status_Effect_Poison";
	}
}
