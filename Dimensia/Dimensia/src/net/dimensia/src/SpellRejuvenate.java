package net.dimensia.src;

/**
 * <code> SpellRejuvenate extends Spell</code>
 * SpellRejuvenate is a special ability that heals the player for 100% of their max health instantly. 
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SpellRejuvenate extends Spell
{
	/**
	 * Constructs a new SpellRejuvenate. No special parameters or values are set in this class, so the constructor 
	 * Spell(int) is the only call.
	 * @param i the ID of this spell (the final ID will be this value + spellIndex)
	 */
	public SpellRejuvenate(int i) 
	{
		super(i);
	}
	
	/**
	 * Overrides ActionbarItem.onRightClick(World, EntityLivingPlayer) to implement the spell effect of SpellRejuvenate - the 
	 * instant full heal. Calling this will fail to use the spell if not enough special energy is available.
	 */
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		if(player.specialEnergy >= cost)
		{
			player.healEntity(world, player.maxHealth);
			player.spendSpecialEnergy(cost);
		}
	}	
}
