package auras;

import entities.EntityLivingPlayer;

public class AuraHeavensReprieve extends Aura
{
	private static final long serialVersionUID = 1L;

	public AuraHeavensReprieve()
	{
		super();
	}
	
	public void onDeath(EntityLivingPlayer player) 
	{
		if(player.health <= 0)
		{
			player.health = player.maxHealth * 0.15f; //give the player 15% health back
			player.invincibilityTicks = 120; //6 seconds immunity
			player.inventory.removeSavingRelic(player); //destory something with that modifier (first thing to occur in the inventory) 
			player.onArmorChange(); //flag the armour as changed
		}
	}
}
