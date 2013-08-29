package transmission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import statuseffects.DisplayableStatusEffect;
import utils.Cooldown;
import utils.DisplayableItemStack;
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
	public int defense;
	public double mana;
	public double health;
	public double specialEnergy;	
	public int maxSpecialEnergy;	
	public int maxHealth;
	public int maxMana;	
	public int selectedSlot;
	public List<DisplayableStatusEffect> statusEffects;	
	public Hashtable<String, Cooldown> cooldowns;
	public DisplayableItemStack[] mainInventory;
	public DisplayableItemStack[] armorInventory;
	public DisplayableItemStack[] quiver;
	public DisplayableItemStack heldMouseItem; 
	
	public TransmittablePlayer()
	{
		entityID = -1;
		defense = 0;
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
