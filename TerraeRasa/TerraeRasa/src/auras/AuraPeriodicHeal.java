package auras;

import statuseffects.StatusEffectRegeneration;
import utils.Damage;
import world.World;
import client.GameEngine;
import entities.EntityPlayer;

/**
 * AuraPeriodicHeal extends Aura to implement healing to the player on damage taken. When the player's hit this aura has
 * a chance to restore some health back, which may be percentile and may be based on the damage taken. If based 
 * on damage taken then it must be percentile. The healAmount for the aura is for the entire status effect. Additionally, 
 * this effect will always tick 5 times.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraPeriodicHeal extends Aura 
{
	private boolean percentile;
	private double healAmount;
	private int durationSeconds;
	private boolean baseOnDamageTaken;
	
	/**
	 * Constructs a new periodic heal aura.
	 * @param cooldownSeconds the cooldown of the aura in seconds
	 * @param durationSeconds the duration of the effect in seconds
	 * @param healAmount the amount healed. This value can either be from 0.0 to 1.0 as a percentage or a flat amount
	 * @param activationChance the chance, from 0.0 to 1.0 of the aura to activate
	 * @param percentile true if the healAmount is a percent of damage taken or max health; otherwise false
	 * @param baseOnDamageTaken true if the healAmount is based on the damage taken; otherwise false
	 */
	public AuraPeriodicHeal(int cooldownSeconds, int durationSeconds, double healAmount, double activationChance, 
			boolean percentile, boolean baseOnDamageTaken)
	{
		super();
		this.maxCooldown = cooldownSeconds * GameEngine.TICKS_PER_SECOND;
		this.durationSeconds = durationSeconds;
		this.percentile = percentile;
		this.healAmount = healAmount;		
		this.activationChance = activationChance;
		this.baseOnDamageTaken = baseOnDamageTaken;
	}

	public void onDamageTaken(World world, EntityPlayer player, Damage damage)
	{
		double healValue = 0;
		if(baseOnDamageTaken) {
			//A percent of damage done
			healValue = damage.amount() * healAmount;
		}
		else {
			if(percentile) {
				//A percent of maximum health
				healValue = player.maxHealth * healAmount;
			}
			else {
				//A static amount
				healValue = healAmount;
			}
		}
		player.registerStatusEffect(world, 
				new StatusEffectRegeneration(durationSeconds, 
						1, 
						healValue / 5, 
						durationSeconds * 4, 
						false));
	}
	
	public String toString()
	{
		if(baseOnDamageTaken) 
			return (int)(100 * activationChance) + "% chance to restore " + (int)(100 * healAmount) + "% of damage taken over " + (durationSeconds) + " seconds";
		else 
			if(percentile) 
				return (int)(100 * activationChance) + "% chance to restore " + (int)(100 * healAmount) + "% of max health over " + (durationSeconds) + " seconds";
			else 
				return (int)(100 * activationChance) + "% chance to restore " + (int)(healAmount) + " health over " + (durationSeconds) + " seconds"; 				
	}
}
