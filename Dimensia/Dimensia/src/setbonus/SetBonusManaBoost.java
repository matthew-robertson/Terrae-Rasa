package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusManaBoost extends SetBonus to increase a player's maximum mana without increasing their intellect. 
 * The increase is based on the power value of the SetBonus, where the power increases the player's mana point for point.
 * IE 1 power = 1 mana.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusManaBoost extends SetBonus
{
	private static final long serialVersionUID = 1L;

	protected SetBonusManaBoost(float power) 
	{
		super(power);
	}
	
	public void apply(EntityLivingPlayer player) 
	{
		player.maxMana += power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.maxHealth -= power;
	}
}
