package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectSteelSkin increases an entity's defense by a given amount. This amount is a an additive flat point
 * increase based on the statuseffect's power, where defense is increased by 1 for every point of power. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectSteelSkin extends StatusEffect
{

	public StatusEffectSteelSkin(double durationSeconds, int tier, double power, int ticksBetweenEffect)
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		iconX = 15;
		iconY = 1;
	}

	public void applyInitialEffect(World world, EntityLiving entity)
	{	
		entity.defense += power;
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.defense -= power;
	}
	
	public String toString()
	{
		return "Increases defense by " + (int)(power);
	}
}
