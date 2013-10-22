package transmission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import statuseffects.StatusEffect;
import utils.Cooldown;
import utils.ItemStack;
import entities.DisplayableEntity;
import entities.IEntityTransmitBase;

public class TransmittablePlayer 
		implements Serializable, IEntityTransmitBase
{
	private static final long serialVersionUID = 1L;
	public int entityID;
	public double x;
	public double y;
	public String playerName;
	public int defense = 0;
	public double mana;
	public double health;
	public double specialEnergy;	
	public int maxSpecialEnergy;		
	public int maxHealth;
	public int maxMana;	
	public int selectedSlot;
	public List<StatusEffect> statusEffects;	
	public Hashtable<String, Cooldown> cooldowns;
	public ItemStack[] mainInventory;
	public ItemStack[] armorInventory;
	public ItemStack[] quiver;
	public ItemStack heldMouseItem; 
	
	public TransmittablePlayer()
	{
		entityID = -1;
		playerName = "";
		x = 0;
		y = 0;
		defense = 0;
		mana = 0;
		health = 0;
		specialEnergy = 0;
		maxSpecialEnergy = 0;
		maxHealth = 0;
		maxMana = 0;
		statusEffects = new ArrayList<StatusEffect>();
		cooldowns = new Hashtable<String, Cooldown>();
		mainInventory = new ItemStack[0];
		armorInventory = new ItemStack[0];
		quiver = new ItemStack[0];
	}
	
	public void addStatusEffects(List<StatusEffect> statusEffects)
	{
		for(StatusEffect effect : statusEffects)
		{
			this.statusEffects.add(effect);
		}
	}

	public void addInventories(ItemStack[] mainInventory, ItemStack[] armorInventory, ItemStack[] quiver) 
	{
		this.mainInventory = new ItemStack[mainInventory.length];
		this.armorInventory = new ItemStack[armorInventory.length];
		this.quiver = new ItemStack[quiver.length];
		
		for(int i = 0; i < mainInventory.length; i++)
		{
			if(mainInventory[i] != null)
			{
				this.mainInventory[i] = new ItemStack(mainInventory[i]);
			}
		}
		for(int i = 0; i < armorInventory.length; i++)
		{
			if(armorInventory[i] != null)
			{
				this.armorInventory[i] = new ItemStack(armorInventory[i]);
			}
		}
		for(int i = 0; i < quiver.length; i++)
		{
			if(quiver[i] != null)
			{
				this.quiver[i] = new ItemStack(quiver[i]);
			}
		}
	}

	public void setHeldItem(ItemStack stack)
	{
		if(stack != null)
		{
			heldMouseItem = new ItemStack(stack);
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
