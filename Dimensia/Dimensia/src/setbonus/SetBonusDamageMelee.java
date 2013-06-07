package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusDamageMelee extends SetBonus
{
	protected SetBonusDamageMelee(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.meleeDamageModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.meleeDamageModifier /= (1 + power);
	}

}
