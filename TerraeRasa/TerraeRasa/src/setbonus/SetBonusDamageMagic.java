package setbonus;

import entities.EntityPlayer;

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

	public SetBonusDamageMagic(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.magicDamageModifier *= (1 + power);
	}

	public void remove(EntityPlayer player) 
	{
		player.magicDamageModifier/= (1 + power);
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Magic Damage";
	}
}
