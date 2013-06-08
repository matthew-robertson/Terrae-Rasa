package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusDamageMagic extends SetBonus
{
	public SetBonusDamageMagic(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.magicDamageModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.magicDamageModifier/= (1 + power);
	}

}
