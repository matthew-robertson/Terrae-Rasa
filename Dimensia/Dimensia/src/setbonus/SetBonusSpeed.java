package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusSpeed extends SetBonus
{
	public SetBonusSpeed(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.movementSpeedModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.movementSpeedModifier /= (1 + power);
	}

}
