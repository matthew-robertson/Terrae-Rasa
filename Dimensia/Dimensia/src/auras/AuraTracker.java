package auras;

import java.io.Serializable;

import utils.InventoryPlayer;
import entities.EntityLivingPlayer;

/**
 * AuraTracker holds all data and methods relating to the player's Auras. In Version 1.0, each piece of armour has its own
 * Aura tracking, so for example equipping a chestplate might activate some sort of aura. {@link #setAurasByPiece(AuraContainer, int)} is used
 * to update a particular slot's AuraContainer. 
 * <br><br>
 * It should be noted that AuraTracker implements IAura - this means that in order to trigger a specific event, such as onDamageDone(),
 * the method call to AuraTracker will be the same as it would be to a specific aura. Therefore, AuraTracker supports all the different
 * events that an individual Aura can have:
 * <ul>
 * <li>{@link #onDamageDone(EntityLivingPlayer)}</li>
 * <li>{@link #onDamageTaken(EntityLivingPlayer)}</li>
 * <li>{@link #onDeath(EntityLivingPlayer)}</li>
 * <li>{@link #onHeal(EntityLivingPlayer)}</li>
 * <li>{@link #onPercentageHealth(EntityLivingPlayer)}</li>
 * <li>{@link #onStatusEffectGained(EntityLivingPlayer)}</li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraTracker 
		implements IAura, Serializable
{
	private static final long serialVersionUID = 1L;	
	private AuraContainer[] aurasByPiece;	
	
	/**
	 * Constructs a new AuraTracker, allocating a slot for each piece of armour the player can equip.
	 */
	public AuraTracker()
	{
		aurasByPiece = new AuraContainer[InventoryPlayer.ARMOR_INVENTORY_LENGTH];
	}

	/**
	 * Sets a given slot to the specified container. A null AuraContainer indicates that instead of giving a slot
	 * auras that slot should be cleared of all auras.
	 * @param container the AuraContainer that will be assigned to a given slot, or null to clear a slot
	 * @param index the index of the slot to assign the AuraContainer to, corresponding to a slot in the player's armour inventory
	 */
	public void setAurasByPiece(AuraContainer container, int index)
	{
		aurasByPiece[index] = container;
	}
	
	/**
	 * Updates all Auras, in all the different slots AuraContainers.
	 * @param player the player to whom this AuraTracker belongs.
	 */
	public void update(EntityLivingPlayer player)
	{
		for(int i = 0; i < aurasByPiece.length; i++)
		{
			if(aurasByPiece[i] != null)
			{
				for(Aura aura : aurasByPiece[i].getAll())
				{
					aura.update(player);
				}
			}
		}
	}
	
	
	public void onDamageDone(EntityLivingPlayer player) 
	{
		for(int i = 0; i < aurasByPiece.length; i++)
		{
			if(aurasByPiece[i] != null)
			{
				for(Aura aura : aurasByPiece[i].getAll())
				{
					aura.onDamageDone(player);
				}
			}
		}
	}
	

	public void onDamageTaken(EntityLivingPlayer player) 
	{
		for(int i = 0; i < aurasByPiece.length; i++)
		{
			if(aurasByPiece[i] != null)
			{
				for(Aura aura : aurasByPiece[i].getAll())
				{
					aura.onDamageTaken(player);
				}
			}
		}
	}
	

	public void onHeal(EntityLivingPlayer player) 
	{
		for(int i = 0; i < aurasByPiece.length; i++)
		{
			if(aurasByPiece[i] != null)
			{
				for(Aura aura : aurasByPiece[i].getAll())
				{
					aura.onHeal(player);
				}
			}
		}
	}
	

	public void onPercentageHealth(EntityLivingPlayer player) 
	{
		for(int i = 0; i < aurasByPiece.length; i++)
		{
			if(aurasByPiece[i] != null)
			{
				for(Aura aura : aurasByPiece[i].getAll())
				{
					aura.onPercentageHealth(player);
				}
			}
		}
	}

	public void onDeath(EntityLivingPlayer player) 
	{
		for(int i = 0; i < aurasByPiece.length; i++)
		{
			if(aurasByPiece[i] != null)
			{
				for(Aura aura : aurasByPiece[i].getAll())
				{
					aura.onDeath(player);
				}
			}
		}
	}
	
	/*

	public void onStatusEffectGained(EntityLivingPlayer player)
	{
		for(int i = 0; i < aurasByPiece.length; i++)
		{
			if(aurasByPiece[i] != null)
			{
				for(Aura aura : aurasByPiece[i].getAll())
				{
					aura.onStatusEffectGained(player);
				}
			}
		}
	}	
	*/
}
