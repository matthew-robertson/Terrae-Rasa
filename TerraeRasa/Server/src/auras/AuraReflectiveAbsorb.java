package auras;

import server.GameEngine;
import statuseffects.StatusEffectAbsorb;
import utils.Damage;
import world.World;
import entities.EntityPlayer;

/**
 * AuraReflectiveAbsorb extends Aura to implement absorbs to the player on damage taken. When the player's hit this aura has
 * a chance to absorb some % of damage, which may be percentile and may be based on the damage taken. If based 
 * on damage taken then it must be percentile. The absorbAmount for the aura is for the entire status effect.  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraReflectiveAbsorb extends Aura 
{
	private static final long serialVersionUID = 1L;
	/** True if the amount absorbed should be based on damage taken or maximum health. It will be based on damage taken if baseOnDamageTaken
	 * is true and maximum health otherwise. */
	private boolean percentile;
	/** The amount absorbed, depending this can be a double value indicating percent or a flat amount. */
	private double absorbAmount;
	/** The duration of the absorb StatusEffect given. */ 
	private int durationSeconds;
	/** True if the absorb amount should be based on damage taken (IE a percentage of damage taken). */
	private boolean baseOnDamageTaken;
	
	/**
	 * Constructs a new AuraReflectiveAbsorb.
	 * @param cooldownSeconds the cooldown of the aura in seconds
	 * @param durationSeconds the duration of the effect in seconds
	 * @param absorbAmount the amount absorbed. This value can either be from 0.0 to 1.0 as a percentage or a flat amount
	 * @param activationChance the chance, from 0.0 to 1.0 of the aura to activate
	 * @param percentile true if the absorbAmount is a percent of damage taken or max health; otherwise false
	 * @param baseOnDamageTaken true if the absorbAmount is based on the damage taken; otherwise false
	 */
	public AuraReflectiveAbsorb(int cooldownSeconds, int durationSeconds, double absorbAmount, double activationChance, 
			boolean percentile, boolean baseOnDamageTaken)
	{
		super();
		this.maxCooldown = cooldownSeconds * GameEngine.TICKS_PER_SECOND;
		this.durationSeconds = durationSeconds;
		this.percentile = percentile;
		this.absorbAmount = absorbAmount;		
		this.activationChance = activationChance;
		this.baseOnDamageTaken = baseOnDamageTaken;
	}

	public void onDamageTaken(World world, EntityPlayer player, Damage damage)
	{
		double absorbValue = 0;
		if(baseOnDamageTaken) 
		{
			//A percent of damage done
			absorbValue = damage.amount() * absorbAmount;
		}
		else 
		{
			if(percentile) 
			{
				//A percent of maximum health
				absorbValue = player.maxHealth * absorbAmount;
			}
			else 
			{
				//A static amount
				absorbValue = absorbAmount;
			}
		}		
		StatusEffectAbsorb effect = new StatusEffectAbsorb(durationSeconds, 1, (int)absorbValue);
		player.registerStatusEffect(world, effect);
	}
	
	public String toString()
	{
		if(baseOnDamageTaken) 
			return (int)(100 * activationChance) + "% chance to absorb " + (int)(100 * absorbAmount) + "% of damage taken.";
		else 
			if(percentile) 
				return (int)(100 * activationChance) + "% chance to absorb " + (int)(100 * absorbAmount) + "% of max health on damage taken";
			else 
				return (int)(100 * activationChance) + "% chance to absorb " + (int)(absorbAmount) + " damage on damage taken"; 				
	}
}
