package net.dimensia.src;

public class StatusEffectRegeneration extends StatusEffect
{
	public StatusEffectRegeneration(float durationSeconds, int tier) 
	{
		super(durationSeconds, tier);
		iconX = 14;
		iconY = 2;
	}

	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		if(ticksLeft % (40 / tier) == 0)
		{
			entity.heal_Textless(world, 4);
		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Regeneration";
	}
}
