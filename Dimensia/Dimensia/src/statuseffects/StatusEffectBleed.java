package statuseffects;

import entities.EntityLiving;
import world.World;

public class StatusEffectBleed extends StatusEffect 
{
	private static final long serialVersionUID = 1L;

	public StatusEffectBleed(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 14;
		iconY = 0;
	}

	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		if(ticksLeft % ticksBetweenEffect == 0)
		{
			entity.damageEntity(world, (int)power, false, false, false);
		}
		ticksLeft--;
	}
	
	public String toString()
	{
		return "Status_Effect_Bleed";
	}
}
