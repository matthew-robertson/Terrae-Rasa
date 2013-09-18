package auras;

import world.World;
import entities.EntityPlayer;

/**
 * AuraStayOfExecution extends Aura to save the player when they die. When onDeath() is called, indicating the
 * player has died, this Aura will attempt to save the player but destroy whatever gave the player this
 * aura. If the player is saved, they will be give 6 seconds immunity and 15% of their max health.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraStayOfExecution extends Aura
{
	private static final long serialVersionUID = 1L;
	/** The strength of this aura, ranging from 0.0D to 1.0D (a 100% heal). */
	private double power;
	
	/**
	 * Constructs a new AuraHeavensReprieve.
	 * @param power a double from 0.0D to 1.0D describing the % of maximum health granted.
	 */
	public AuraStayOfExecution(double power)
	{
		super();
		this.power = power;
	}
	
	/**
	 * Saves the player when they die.
	 */
	public void onDeath(World world, EntityPlayer player) 
	{
		if(player.getHealth() <= 0)
		{
			player.setHealth(player.maxHealth * power); //give the player 15% health back
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
		return "Grants " + (int)(power * 100) + "% of maximum health on death.";
	}
}
