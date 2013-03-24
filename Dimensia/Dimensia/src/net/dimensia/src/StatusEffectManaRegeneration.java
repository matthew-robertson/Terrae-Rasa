package net.dimensia.src;

public class StatusEffectManaRegeneration extends StatusEffect
{
	public StatusEffectManaRegeneration(float durationSeconds, int tier)
	{
		super(durationSeconds, tier);
		iconX = 13;
		iconY = 2;
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		try
		{
			if(ticksLeft % (40 / tier) == 0)
			{
				((EntityLivingPlayer)(entity)).restorePlayerMana(world, 4);
			}
		}
		catch(Exception e) //This should indicate that somehow a non-player has somehow got this buff. Mana cap no longer matters in this case
		{
			entity.mana += 4;
		}
		
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Mana_Regeneration";
	}
}
