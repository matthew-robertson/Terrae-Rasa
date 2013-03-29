package net.dimensia.src;

public class SpellBulwark extends Spell
{
	private static final long serialVersionUID = 1L;

	public SpellBulwark(int i) 
	{
		super(i);
	}

	public void onRightClick(World world, EntityLivingPlayer player)
	{
		if(player.specialEnergy >= cost)
		{
			player.registerStatusEffect(new StatusEffectAbsorb(8, 1, (int) (player.maxHealth * 1.5)));
			player.spendSpecialEnergy(cost);
		}	
	}
}
