package setbonus;

import entities.EntityPlayer;

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

	public SetBonusDamageAll(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.allDamageModifier *= (1 + power);
	}

	public void remove(EntityPlayer player) 
	{
		player.allDamageModifier /= (1 + power);
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Damage";
	}
}
