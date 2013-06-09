package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusDamageAll extends SetBonus to provide changes to a player's damage modifier (all types). This allows an item to increase 
 * the player's damage by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusDamageAll extends SetBonus
{
	private static final long serialVersionUID = 1L;

	public SetBonusDamageAll(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.allDamageModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.allDamageModifier /= (1 + power);
	}

}
