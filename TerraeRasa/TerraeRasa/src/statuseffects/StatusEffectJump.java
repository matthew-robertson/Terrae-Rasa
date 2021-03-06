package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * StatusEffectJump increases an entity's jump height by a given number of blocks. This amount is a an additive value
 * increase based on the statuseffect's power, where the entity can jump 1 block higher for every power unit. This has no
 * reasonable cap but eventually is suicide without fall height being increased. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectJump extends StatusEffect
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new StatusEffectJump, allowing an entity to jump a different height, based on the given power.
	 * @param durationSeconds the double duration of the effect in seconds
	 * @param tier the int tier of the effect
	 * @param power the int increase in block jump height
	 */
	public StatusEffectJump(double durationSeconds, int tier, int power) 
	{
		super(durationSeconds, tier, power, 1);
		this.isBeneficialEffect = false;
		iconX = 0;
		iconY = 0;
	}

	public void applyInitialEffect(World world, EntityLiving entity)
	{	
		entity.setUpwardJumpHeight(entity.getUpwardJumpHeight() + (power * 6));
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{	
		entity.setUpwardJumpHeight(entity.getUpwardJumpHeight() - (power * 6));
	}
	
	public String toString()
	{
		return "Increases jump height by " + (int)(power) + " blocks";
	}
}
