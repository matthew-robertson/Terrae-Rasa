package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusStrength extends SetBonus
{
	protected SetBonusStrength(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.strength += power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.strength -= power;
	}

}
