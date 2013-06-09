package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusDamageMelee extends SetBonus to provide changes to a player's melee damage modifier (melee damage type). This allows an item 
 * to increase the player's melee damage by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusDamageMelee extends SetBonus
{
	private static final long serialVersionUID = 1L;

	public SetBonusDamageMelee(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.meleeDamageModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.meleeDamageModifier /= (1 + power);
	}

}
