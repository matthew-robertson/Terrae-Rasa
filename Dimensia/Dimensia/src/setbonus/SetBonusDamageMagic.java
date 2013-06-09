package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusDamageMagic extends SetBonus to provide changes to a player's magic damage modifier (magic damage type). This allows 
 * an item to increase  the player's magic damage by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusDamageMagic extends SetBonus
{
	private static final long serialVersionUID = 1L;

	public SetBonusDamageMagic(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.magicDamageModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.magicDamageModifier/= (1 + power);
	}

}
