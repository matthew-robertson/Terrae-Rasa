package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusKnockback extends SetBonus 
{
	public SetBonusKnockback(float power)
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
	/**
	 * @param player the player whom is activating the setbonus
	 */
	
}
