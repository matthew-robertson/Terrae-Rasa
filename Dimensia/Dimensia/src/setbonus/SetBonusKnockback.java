package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusKnockback extends SetBonus to provide changes to a player's knockback ability. This allows an item to increase the player's
 * overall knockback aoplied to an enemy when hitting them by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusKnockback extends SetBonus 
{
	private static final long serialVersionUID = 1L;

	public SetBonusKnockback(double power)
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.knockbackModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.knockbackModifier /= (1 + power);
	}	
	
	public String toString()
	{
		return "+" + (int)(power * 100) + "% Knockback";
	}
}
