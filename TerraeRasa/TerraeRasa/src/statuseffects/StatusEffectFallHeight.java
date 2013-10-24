package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectFallHeight increases an entity's fall height by a given number of blocks. This amount is a an additive value
 * increase based on the statuseffect's power, where the entity can fall 1 more block for every power unit. This effect has
 * no actual cap, but in practice it can reach values where there is little extra benefit to the increased distance.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectFallHeight extends StatusEffect
{

	/**
	 * 
	 * @param durationSeconds
	 * @param tier
	 * @param power increase in blocks 
	 * @param ticksBetweenEffect
	 */
	public StatusEffectFallHeight(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		this.isBeneficialEffect = false;
		iconX = 0;
		iconY = 0;
	}

	public void applyInitialEffect(World world, EntityLiving entity)
	{	
		entity.setMaxHeightFallenSafely(entity.getMaxHeightFallenSafely() + (6 * power));
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.setMaxHeightFallenSafely(entity.getMaxHeightFallenSafely() - (6 * power));
	}
	
	public String toString()
	{
		return "Increases fall height by " + (int)(power) + "blocks";
	}
}
