package passivebonuses;

import server.entities.EntityPlayer;

/**
 * PassiveBonusKnockback extends PassiveBonus to provide changes to a player's knockback ability. This allows an item to increase the player's
 * overall knockback aoplied to an enemy when hitting them by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusKnockback extends PassiveBonus 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusKnockback with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusKnockback(double power)
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.knockbackModifier *= (1 + power);
	}

	public void remove(EntityPlayer player) 
	{
		player.knockbackModifier /= (1 + power);
	}	
	
	public String toString()
	{
		return "+" + (int)(power * 100) + "% Knockback";
	}
}
