package net.dimensia.src;

public class SpellRejuvenate extends Spell
{
	private static final long serialVersionUID = 1L;

	public SpellRejuvenate(int i) 
	{
		super(i);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		if(player.specialEnergy >= cost)
		{
			player.healEntity(world, player.maxHealth);
			player.spendSpecialEnergy(cost);
		}
	}	
}
