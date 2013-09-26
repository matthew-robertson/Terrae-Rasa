package spells;

import entities.EntityPlayer;
import statuseffects.StatusEffectAbsorb;
import world.World;

/**
 * SpellBulwark is a special ability that shields the player for 100% of their max health over 8 seconds, costing 55 special energy. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SpellBulwark extends Spell
{
	/** The absorb percent, as a value between 0D and 1D. */
	private static final double ABSORB_POWER = 1.0;
	/** The special energy cost in points (players default to having 100 maximum special energy). */
	private static final int SPECIAL_COST = 55;
	/** The resource type of this spell, in this case special energy. */
	private static final int RESOURCE_TYPE = RESOURCE_SPECIAL;
	/** The name of this spell - Bulwark. */
	private static final String NAME = "Bulwark";
	/** The duration of the absorb in seconds. */
	private static final int ABSORB_DURATION_SEC = 8;
	/** The spell description. */
	private static final String TOOLTIP = "Shields the player for " + (int)(100 * ABSORB_POWER) + "% of max health, lasting " + ABSORB_DURATION_SEC
			+ " seconds. Costs " + SPECIAL_COST + "% Special Energy";
	
	/**
	 * Constructs a new SpellBulwark. 
	 * @param i the ID of this spell (the final ID will be this value + spellIndex)
	 */
	protected SpellBulwark(int i) 
	{
		super(i);
		setCost(SPECIAL_COST);
		setResourceType(RESOURCE_TYPE);
		setName(NAME);
		setExtraTooltipInformation(TOOLTIP);
	}
	
	/**
	 * Overrides ActionbarItem.onRightClick(World, EntityPlayer) to implement the spell effect of SpellBulwark - a brief absorb 
	 * shield. Calling this will fail to use the spell if not enough special energy is available.
	 */
	public void onRightClick(World world, EntityPlayer player)
	{
		if(player.specialEnergy >= cost)
		{
			player.registerStatusEffect(world, new StatusEffectAbsorb(ABSORB_DURATION_SEC, 1, (int) (ABSORB_POWER * player.maxHealth)));
			player.spendSpecialEnergy(cost);
		}	
	}
}
