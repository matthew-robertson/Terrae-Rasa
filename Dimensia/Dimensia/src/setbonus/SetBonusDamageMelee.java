package setbonus;

import entities.EntityPlayer;

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

	public SetBonusDamageMelee(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.meleeDamageModifier *= (1 + power);
	}

	public void remove(EntityPlayer player) 
	{
		player.meleeDamageModifier /= (1 + power);
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Melee Damage";
	}
}
