package statuseffects;

import entities.EntityLiving;
import world.World;

public class StatusEffectRegeneration extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	public StatusEffectRegeneration(float durationSeconds, int tier, float power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 14;
		iconY = 2;
	}

	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		if(ticksLeft % ticksBetweenEffect == 0)
		{
			entity.heal(world, (int)power, false);
		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Regeneration";
	}
}
