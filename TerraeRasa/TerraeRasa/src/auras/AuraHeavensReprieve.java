package auras;

import world.World;
import entities.EntityPlayer;

/**
 * AuraHeavensReprieve extends Aura to save the player when they die. When onDeath() is called, indicating the
 * player has died, this Aura will attempt to save the player but destroy whatever gave the player this
 * aura. If the player is saved, they will be give 6 seconds immunity and 15% of their max health.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraHeavensReprieve extends Aura
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new AuraHeavensReprieve.
	 */
	public AuraHeavensReprieve()
	{
		super();
	}
	
	/**
	 * Saves the player when they die.
	 */
	public void onDeath(World world, EntityPlayer player) 
	{
		if(player.health <= 0)
		{
			player.health = player.maxHealth * 0.15; //give the player 15% health back
			player.invincibilityTicks = 120; //6 seconds immunity
			player.inventory.removeSavingRelic(player); //destory something with that modifier (first thing to occur in the inventory) 
			player.onArmorChange(); //flag the armour as changed
		}
	}
	
	/**
	 * Overrides Aura.toString() to accurately describe this aura in text form.
	 */
	public String toString()
	{
		return "Heaven's Reprieve";
	}
}
