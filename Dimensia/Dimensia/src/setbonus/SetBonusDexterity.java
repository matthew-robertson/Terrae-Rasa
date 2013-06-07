package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusDexterity extends SetBonus
{
	protected SetBonusDexterity(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.dexterity += power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.dexterity -= power;
	}

}
