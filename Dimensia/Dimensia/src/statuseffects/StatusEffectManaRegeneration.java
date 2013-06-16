package statuseffects;

import world.World;
import entities.EntityLiving;

public class StatusEffectManaRegeneration extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	public StatusEffectManaRegeneration(double durationSeconds, int tier, double power, int ticksBetweenEffect)
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 13;
		iconY = 2;
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		if(ticksLeft % ticksBetweenEffect == 0)
		{
			entity.restoreMana(world, (int)power, false);
		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Mana_Regeneration";
	}
}
