package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonusManaBoost extends PassiveBonus to increase a player's maximum mana without increasing their intellect. 
 * The increase is based on the power value of the PassiveBonus, where the power increases the player's mana point for point.
 * IE 1 power = 1 mana.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusManaBoost extends PassiveBonus
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusManaBoost with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusManaBoost(double power) 
	{
		super(power);
	}
	
	public void apply(EntityPlayer player) 
	{
		player.temporaryMaxMana += power;
	}

	public void remove(EntityPlayer player) 
	{
		player.temporaryMaxMana -= power;
	}
	
	public String toString()
	{
		return "+" + (int)(power) + " maximum mana";
	}
}
