package transmission;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import passivebonuses.PassiveBonusContainer;
import statuseffects.StatusEffect;
import statuseffects.StatusEffectAbsorb;
import utils.Cooldown;
import utils.InventoryPlayer;
import auras.AuraTracker;
import entities.Entity;
import enums.EnumPlayerDifficulty;

public class CompressedPlayer extends Entity
		implements Serializable
{
	public static final long serialVersionUID = 1L;
		
	public double attackSpeedModifier;
	public double knockbackModifier;
	public double meleeDamageModifier;
	public double rangeDamageModifier;
	public double magicDamageModifier;
	public double allDamageModifier;	
	public List<StatusEffect> statusEffects;	
	public double criticalStrikeChance; 
	public double dodgeChance;
	public boolean isImmuneToFallDamage;
	public int invincibilityTicks;	
	public int maxHealth;
	public int maxMana;
	public double mana;
	public double defense;
	public double health;
	public List<StatusEffectAbsorb> absorbs;
	
	public boolean isSwingingRight;
	public boolean hasSwungTool;
	public double rotateAngle;
	public boolean armorChanged;
	public int ticksSinceLastCast;
	public int ticksInCombat;
	public int ticksOfHealthRegen;
	public boolean isInCombat;
	public boolean isMining;
	public boolean isReloaded;
	public int ticksreq;
	public int sx;
	public int sy;		
	public String playerName;
	public boolean inventoryChanged;
	public EnumPlayerDifficulty difficulty;

	public Hashtable<String, Cooldown> cooldowns;
	public int baseSpecialEnergy;
	public Dictionary<String, Boolean> nearBlock;
	public PassiveBonusContainer currentBonuses; 
	public AuraTracker auraTracker;
	
	public int strength;
	public int dexterity;
	public int intellect;
	public int stamina;
	
	public int temporarySpecialEnergy;
	public double specialEnergy;
	public double maxSpecialEnergy;
	
	public int viewedChestX;
	public int viewedChestY;
	public boolean isViewingChest;	
	public int baseMaxHealth;
	public int temporaryMaxHealth;
	public int baseMaxMana;	
	public int temporaryMaxMana;
	public double respawnXPos;
	public double respawnYPos;	
	
	public int selectedRecipe;
	public int selectedSlot;
	public boolean isFacingRight;
	public boolean isInventoryOpen;	
	public InventoryPlayer inventory;
	
	public double healthRegenerationModifier;
	public double manaRegenerationModifier;
	public double specialRegenerationModifier;
	
	public double pickupRangeModifier;
	public double staminaModifier;
	public double intellectModifier;
	public double dexterityModifier;
	public double strengthModifier;
	
	public boolean defeated;
	
}
