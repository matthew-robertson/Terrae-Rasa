package transmission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import entities.DisplayableEntity;
import entities.IEntityTransmitBase;

import statuseffects.DisplayableStatusEffect;
import statuseffects.StatusEffect;
import utils.Cooldown;
import utils.DisplayableItemStack;
import utils.ItemStack;

public class TransmittablePlayer 
		implements Serializable, IEntityTransmitBase
{
	private static final long serialVersionUID = 1L;
	public int entityID;
	public double x;
	public double y;
	public String playerName;
	public double mana;
	public double health;
	public double specialEnergy;	
	public int maxSpecialEnergy;		
	public int maxHealth;
	public int maxMana;	
	public List<DisplayableStatusEffect> statusEffects;	
	public Hashtable<String, Cooldown> cooldowns;
	public DisplayableItemStack[] mainInventory;
	public DisplayableItemStack[] armorInventory;
	public DisplayableItemStack[] quiver;
	
	public TransmittablePlayer()
	{
		entityID = -1;
		playerName = "";
		x = 0;
		y = 0;
		mana = 0;
		health = 0;
		specialEnergy = 0;
		maxSpecialEnergy = 0;
		maxHealth = 0;
		maxMana = 0;
		statusEffects = new ArrayList<DisplayableStatusEffect>();
		cooldowns = new Hashtable<String, Cooldown>();
		mainInventory = new DisplayableItemStack[0];
		armorInventory = new DisplayableItemStack[0];
		quiver = new DisplayableItemStack[0];
	}
	
	public void addStatusEffects(List<StatusEffect> statusEffects)
	{
		for(StatusEffect effect : statusEffects)
		{
			this.statusEffects.add(new DisplayableStatusEffect(effect));
		}
	}

	public void addInventories(ItemStack[] mainInventory, ItemStack[] armorInventory, ItemStack[] quiver) 
	{
		this.mainInventory = new DisplayableItemStack[mainInventory.length];
		this.armorInventory = new DisplayableItemStack[armorInventory.length];
		this.quiver = new DisplayableItemStack[quiver.length];
		
		for(int i = 0; i < mainInventory.length; i++)
		{
			if(mainInventory[i] != null)
			{
				this.mainInventory[i] = new DisplayableItemStack(mainInventory[i]);
			}
		}
		for(int i = 0; i < armorInventory.length; i++)
		{
			if(armorInventory[i] != null)
			{
				this.armorInventory[i] = new DisplayableItemStack(armorInventory[i]);
			}
		}
		for(int i = 0; i < quiver.length; i++)
		{
			if(quiver[i] != null)
			{
				this.quiver[i] = new DisplayableItemStack(quiver[i]);
			}
		}
	}

	@Override
	public int getEntityID() {
		return entityID;
	}

	@Override
	public int getEntityType() {
		return DisplayableEntity.TYPE_PLAYER;
	}

	@Override
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		
	}
}
