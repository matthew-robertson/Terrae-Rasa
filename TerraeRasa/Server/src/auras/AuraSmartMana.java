package auras;

import server.GameEngine;
import world.World;
import entities.EntityPlayer;

/**
 * AuraSmartMana extends Aura to implement mana regeneration to the player when they are below a certain 
 * mana percentage. When the player's mana is below this amount, and the aura is not on cooldown, they 
 * will be given the specified amount of mana - which may be percentile. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraSmartMana extends Aura 
{
	private boolean percentile;
	private double manaAmount;
	private double activationThreshold;
	
	/**
	 * Constructs a new smart mana aura.
	 * @param cooldownSeconds the cooldown of the aura in seconds
	 * @param manaAmount a flat amount; or value from 0.0-1.0 if mana regeneration is percentage
	 * @param activationThreshold a value from 0.0-1.0 indicating the percentage of mana to activate at
	 * @param percentile true if mana regeneration is based on a percent of maximum mana; otherwise false
	 */
	public AuraSmartMana(int cooldownSeconds, double manaAmount, double activationThreshold, boolean percentile)
	{
		super();
		maxCooldown = cooldownSeconds * GameEngine.TICKS_PER_SECOND;
		this.percentile = percentile;
		this.activationThreshold = activationThreshold;
		this.manaAmount = manaAmount;		
	}
	
	public void onPercentageMana(World world, EntityPlayer player)
	{
		if(remainingCooldown <= 0 && (player.getManaPercent() <= activationThreshold))
		{
			double restore = (percentile) ? (this.manaAmount * player.maxMana) : this.manaAmount;
			player.restoreMana(world, (int)restore, true);
			remainingCooldown = maxCooldown;
		}
	}
	
	public String toString()
	{
		return "Restores " + (int)manaAmount + " mana below " + (int)(100 * activationThreshold) + "%"; 
	}
}
