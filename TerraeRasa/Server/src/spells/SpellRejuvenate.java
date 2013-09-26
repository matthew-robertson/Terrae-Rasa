package spells;

import entities.EntityPlayer;
import world.World;

/**
 * SpellRejuvenate is a special ability that heals the player for 80% of their max health instantly at the cost of 100% special energy. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SpellRejuvenate extends Spell
{
	/** The heal percent, as a value between 0D and 1D. */
	private static final double HEAL_POWER = 0.8;
	/** The special energy cost in points (players default to having 100 maximum special energy). */
	private static final int SPECIAL_COST = 100;
	/** The resource type of this spell, in this case special energy. */
	private static final int RESOURCE_TYPE = RESOURCE_SPECIAL;
	/** The name of this spell - Rejuvenate. */
	private static final String NAME = "Rejuvenate";
	/** The spell description. */
	private static final String TOOLTIP = "Restores " + (int)(100 * HEAL_POWER) + "% of the player's maximum health immediately. Costs " + SPECIAL_COST + " Special Energy.";
	
	
	/**
	 * Constructs a new SpellRejuvenate. 
	 * @param i the ID of this spell (the final ID will be this value + spellIndex)
	 */
	protected SpellRejuvenate(int i) 
	{
		super(i);
		setCost(SPECIAL_COST);
		setResourceType(RESOURCE_TYPE);
		setName(NAME);
		setExtraTooltipInformation(TOOLTIP);
	}
	
	/**
	 * Overrides ActionbarItem.onRightClick(World, EntityPlayer) to implement the spell effect of SpellRejuvenate. This is an instant healing effect
	 * at the cost of special energy. Calling this will fail to use the spell if not enough special energy is available.
	 */
	public void onRightClick(World world, EntityPlayer player)
	{
		if(player.specialEnergy >= cost)
		{
			player.heal(world, (int) (HEAL_POWER * player.maxHealth), true);
			player.spendSpecialEnergy(cost);
		}
	}	
}
