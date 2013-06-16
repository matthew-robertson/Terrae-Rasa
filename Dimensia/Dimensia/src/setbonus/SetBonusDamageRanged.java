package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusDamageRange extends SetBonus to provide changes to a player's damage modifier (range damage type). This allows an item to increase 
 * the player's range damage by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusDamageRanged extends SetBonus
{
	private static final long serialVersionUID = 1L;

	public SetBonusDamageRanged(double power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.rangeDamageModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.rangeDamageModifier /= (1 + power);
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Range Damage";
	}
}
