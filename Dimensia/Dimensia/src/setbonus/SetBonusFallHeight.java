package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusFallHeight extends SetBonus
{
	public SetBonusFallHeight(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.maxHeightFallenSafely += power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.maxHeightFallenSafely -= power; 
	}

}
