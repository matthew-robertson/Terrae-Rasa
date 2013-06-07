package spells;

import entities.EntityLivingPlayer;
import statuseffects.StatusEffectAbsorb;
import world.World;

/**
 * <code> SpellBulwark extends Spell</code>
 * SpellBulwark is a special ability that shields the player for 150% of their max health over 8 seconds. 
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SpellBulwark extends Spell
{
	/**
	 * Constructs a new SpellBulwark. No special parameters or values are set in this class, so the constructor 
	 * Spell(int) is the only call.
	 * @param i the ID of this spell (the final ID will be this value + spellIndex)
	 */
	public SpellBulwark(int i) 
	{
		super(i);
	}
	
	/**
	 * Overrides ActionbarItem.onRightClick(World, EntityLivingPlayer) to implement the spell effect of SpellBulwark - the 
	 * absorb shield. Calling this will fail to use the spell if not enough special energy is available.
	 */
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		if(player.specialEnergy >= cost)
		{
			player.registerStatusEffect(new StatusEffectAbsorb(8, 1, (int) (player.maxHealth * 1.5)));
			player.spendSpecialEnergy(cost);
		}	
	}
}
