package entities;
import items.Item;
import items.ItemAmmo;
import items.ItemArmor;
import items.ItemMagic;
import items.ItemRanged;
import items.ItemThrown;
import items.ItemTool;
import items.ItemToolAxe;
import items.ItemToolHammer;
import items.ItemToolPickaxe;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusContainer;
import passivebonuses.PassiveBonusFactory;
import server.TerraeRasa;
import statuseffects.StatusEffect;
import statuseffects.StatusEffectAbsorb;
import transmission.CompressedPlayer;
import transmission.EntityUpdate;
import transmission.ServerUpdate;
import transmission.StatUpdate;
import utils.Cooldown;
import utils.Damage;
import utils.GemSocket;
import utils.InventoryPlayer;
import utils.ItemStack;
import utils.MathHelper;
import utils.Vector2F;
import world.World;
import auras.Aura;
import auras.AuraContainer;
import auras.AuraTracker;
import blocks.Block;
import enums.EnumColor;
import enums.EnumDamageSource;
import enums.EnumDamageType;
import enums.EnumPlayerDifficulty;
import enums.EnumToolMaterial;

/**
 * <br><br>
 * EntityPlayer implements all the features needed for the user's character. 
 * <br><br>
 * There are several methods of significance in EntityPlayer, serving different purposes. 
 * The first is {@link #onWorldTick(World)}. This method should be called every single world tick,
 * and this is how the player gets updated regularly. This should apply things like regen, gravity,
 * and update cooldowns or invincibility (things based on world ticks). onWorldTick(World) can also
 * do things like check nearby blocks, to see if recipes need updated. Anything that should be 
 * done regularly, but not every frame, essentially.
 * <br><br>
 * A second major component of <code>EntityPlayer</code> is recipe updating. The Recipe[] used are based off 
 * of what craftManager decides are possible for the inventory object passed to it. 
 * <code>EntityPlayer</code> provides a method to check for nearby blocks, and update recipes as required 
 * {@link #checkForNearbyBlocks(World)}, based on the result for craftingManager. 
 * <br><br>
 * The third major component of <code>EntityPlayer</code> is armour management. This is done almost 
 * automatically through a single method - {@link #onArmorChange()}. This method recalculates the armor
 * bonuses of the armour, as well as the set bonuses. It then proceeds to cancel the previous ones. 
 * <b>NOTE: This method does dangerous things, and more than likely is going to terribly ruin any attempt 
 * at adding a +defense potion, or -defense debuff. It WILL have to be recoded, with little doubt.</b>
 * <br><br>
 * The final major component of <code>EntityPlayer</code> is block breaking, through the method 
 * {@link #breakBlock(World, int, int, Item)}. This method is what handles mining, and any 
 * gradual block breaking done by the player. A block break is automatically send to the 'world map' upon
 * completion of the mining.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0 
 * @since       1.0
 */
public class EntityPlayer extends EntityLiving
{
	private static final long serialVersionUID = 1L;

	/** This value apparently has to be negative. */
	private static final double leftSwingBound = MathHelper.degreeToRadian(-135); 
	private static final double rightSwingBound = MathHelper.degreeToRadian(55);
	private static final double totalSwingArcRad = Math.abs(leftSwingBound) + Math.abs(rightSwingBound);
 	private static final double totalSwingArcDeg = MathHelper.radianToDegree(totalSwingArcRad);
	private static final int HEALTH_FROM_STAMINA = 10;
	private static final int MANA_FROM_INTELLECT = 10;
	private static final double DAMAGE_BONUS_INTELLECT = 1.0 / 100;
	private static final double DAMAGE_BONUS_DEXTERITY = 1.0 / 100;
	private static final double DAMAGE_BONUS_STRENGTH = 1.0 / 100;
	private boolean isSwingingRight;
	private boolean hasSwungTool;
	private double rotateAngle;
	private boolean armorChanged;
	private int ticksSinceLastCast;
	private int ticksInCombat;
	private int ticksOfHealthRegen;
	private boolean isInCombat;
	private boolean isMining;
	private boolean isReloaded;
	private int ticksreq;
	private int sx;
	private int sy;		
	private final String playerName;
	private boolean inventoryChanged;
	private final EnumPlayerDifficulty difficulty;
	private final int MAXIMUM_BASE_MANA = 400;
	private final int MAXIMUM_BASE_HEALTH = 400;
	private final int MAX_BLOCK_PLACE_DISTANCE = 42;
	private Hashtable<String, Cooldown> cooldowns;
	private int baseSpecialEnergy;
	private Dictionary<String, Boolean> nearBlock;
	private PassiveBonusContainer currentBonuses; 
	private AuraTracker auraTracker;
	
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
	
	private boolean statsNeedResent;
	
	/** A flag indicating if the player has been forever defeated. If they have, they will not be saved to disk.*/
	public boolean defeated;
	
	/**
	 * Constructs a new instance of EntityPlayer with default settings, inventory (includes 3 basic tools),
	 * and the specified name/difficulty. The default reset position is set to (50, 0), but this
	 * value is essentially worthless, and should be reset.
	 * @param name the player's name
	 * @param difficulty the difficulty setting the player has selected (EnumDifficulty)
	 */
	public EntityPlayer(String name, EnumPlayerDifficulty difficulty)
	{
		super();		
		stamina = 0;
		specialEnergy = 100;
		isJumping = false;
		isMining = false;
		setMovementSpeedModifier(1.0f);		
		setBaseSpeed(5.0f);
		width = 12;
		height = 18;
		baseSpecialEnergy = 100;
		maxSpecialEnergy = 100;
		temporarySpecialEnergy = 0;
		setMaxHeightFallenSafely(78);
		blockHeight = 3;
		blockWidth = 2;
		setRespawnPosition(50, 0);		
		setUpwardJumpHeight(48);
		ticksSinceLastCast = 99999;
		upwardJumpCounter = 0;
		jumpSpeed = 5;
		canJumpAgain = true;
		inventory = new InventoryPlayer();
		playerName = name;
		rotateAngle = -120.0f;		
		isInCombat = false;
		ticksInCombat = 0;
		ticksSinceLastCast = 0;
		health = 100;
		maxHealth = 100;
		baseMaxHealth = 100;
		baseMaxMana = 0;
		maxMana = 0;
		this.difficulty = difficulty;
		invincibilityTicks = 10;
		selectedSlot = 0;
		viewedChestX = 0;
		viewedChestY = 0;
		defeated = false;
		inventoryChanged = true;	
		selectedRecipe = 0;
		knockbackModifier = 1;
		meleeDamageModifier = 1;
		rangeDamageModifier = 1;
		magicDamageModifier = 1;
		healthRegenerationModifier = 1;
		manaRegenerationModifier = 1;
		specialRegenerationModifier = 1;
		allDamageModifier = 1;
		pickupRangeModifier = 1;
		staminaModifier = 1;
		intellectModifier = 1;
		dexterityModifier = 1;
		strengthModifier = 1;
		isReloaded = false;
		cooldowns = new Hashtable<String, Cooldown>();
		nearBlock = new Hashtable<String, Boolean>();
		
		nearBlock.put(Block.none.getName(), true);
		
		auraTracker = new AuraTracker();
		
		jumpSound = "Player Jump";
		deathSound = "Player Death";
	}
	
	public EntityPlayer(EntityPlayer entity)
	{
		throw new RuntimeException("Support Not Implemented - Was this action actually required?");
	}
	
	/**
	 * Fixes issues from loading a player from disk. This generally relates to applying newer recipes and content, so that the
	 * player can actually view and use them.
	 */
	public void reconstructPlayerFromFile()
	{	
		isReloaded = true;
		rotateAngle = -120.0f;			
		invincibilityTicks = 10;
		selectedSlot = 0;
		viewedChestX = 0;
		viewedChestY = 0;
		isViewingChest = false;
		isMining = false;
		inventoryChanged = true;	
		selectedRecipe = 0;
		nearBlock = new Hashtable<String, Boolean>();

		nearBlock.put(Block.none.getName(), true);

		auraTracker = new AuraTracker();
	}
	
	/**
	 * Updates the player, should only be called each world tick.
	 */
	public void onWorldTick(ServerUpdate update, World world)
	{		
		invincibilityTicks = (invincibilityTicks > 0) ? --invincibilityTicks : 0;
		ticksSinceLastCast++;
		if(armorChanged)
		{
			refreshPassiveBonuses();
			recalculateStats();
			armorChanged = false;
		}		
		checkForCombatStatus();
		checkAndUpdateStatusEffects(world);
		applyGravity(world); //Apply Gravity to the player (and jumping)
		if(world.getWorldTime() % 20 == 0) {
			applyHealthRegen(world);
			applyManaRegen(world);
			applySpecialRegen(world);
		}
		checkForNearbyBlocks(world);	
		verifyChestRange();
		updateCooldowns();
		auraTracker.update(world, this);
		auraTracker.onTick(world, this);
		if(isDead())
		{
			onDeath(world);
		}
		if(statsNeedResent)
		{
			statsNeedResent = false;
			update.addStatUpdate(getStats());
		}
	}
	
	/**
	 * Updates time in combat, and if the time has exceeded 120 ticks (6 seconds) since the last combat action, combat status is removed.
	 */
	private void checkForCombatStatus()
	{
		ticksInCombat++;
		
		if(ticksInCombat > 120)
		{
			ticksInCombat = 0;
			isInCombat = false;
		}
	}
	
	/**
	 * Checks if the player is still near their selected chest (should there be one selected). If the player is not near that
	 * chest, it is cleared and no longer rendered.
	 */
	private void verifyChestRange() 
	{
		if(isViewingChest)
		{
			if(MathHelper.distanceBetweenTwoPoints(viewedChestX * 6 + 6, viewedChestY * 6 + 6, x + (blockWidth / 2), y + (blockHeight / 2)) >= 48)
			{
				clearViewedChest();
			}			
		}
	}

	/**
	 * Gets the player's name, assigned upon player creation
	 * @return the player's name
	 */
	public final String getName()
	{
		return playerName;
	}
		
	public void setIsMining(boolean b){
		isMining = b;
	}
	
	/**
	 * Sets the respawn position of the player (this may be broken between world sizes)
	 * @param x the x position in ortho units
	 * @param y the y position in ortho units
	 */
	public void setRespawnPosition(int x, int y) 
	{
		respawnXPos = x;		
		if(x % 6 != 0)
		{
			x = (int)(x / 6);
		}		
		respawnYPos = y;
	}
	
	/**
	 * Applies gravity or a jump upward, depending on if the entity is jumping
	 */
	public void applyGravity(World world) 
	{
		if(isOnGround(world)) //Is the entity on the ground? If so they can jump again
		{
			//Check for fall damage
			if((isJumping || !canJumpAgain) && 
				isAffectedByGravity && 
				!isImmuneToFallDamage && 
				distanceFallen > getMaxHeightFallenSafely()) 
			{
				//calculate the fall damage
				double fallDamage = MathHelper.getFallDamage(jumpSpeed, world.g, ticksFallen); 
				if(fallDamage > 0)
				{
					damage(world, 
							new Damage(fallDamage, 
									new EnumDamageType[] { EnumDamageType.FALL }, 
									EnumDamageSource.FALL).setIsDodgeable(false)
									.setPenetratesArmor(true), 
							true);
				}
			}
			
			ticksFallen = 0;
			canJumpAgain = true;
			distanceFallen = 0;
		}
		
		if(isJumping) //If the entity is jumping upwards, move them up
		{
			moveEntityUp(world, jumpSpeed * getMovementSpeedModifier());			
		}
		else if(!isOnGround(world) && isAffectedByGravity) //otherwise, if the entity is in the air, make them fall
		{
			moveEntityDown(world, MathHelper.getFallSpeed(jumpSpeed, world.g, ticksFallen));
			ticksFallen++;
		}	
	}
	
	/**
	 * Overrides the entity damage taken to account for different invincibility and death
	 * @param world the world the player is currently in
	 * @param damage the damage the player will take 
	 * @param showWorldText true if the damage should be shown as world text, otherwise false
	 */
	public void damage(World world, Damage damage, boolean showWorldText)
	{
		if(invincibilityTicks <= 0) //If it's possible to take damage
		{
			//Check if the damage can be dodged, then attempt a roll to see if it will be dodged
			if(damage.isDodgeable() && (Math.random() < dodgeChance || dodgeChance >= 1.0f)) 
			{
				//Render world text for the dodge if applicable
				if(showWorldText)
				{
					world.addTemporaryText("Dodge", (int)x - 2, (int)y - 3, 20, EnumColor.GREEN); 
				}
			}
			else 
			{
				//Trigger the onDamageTaken() effect of auras before calculating damage after armour and other
				//effects
				auraTracker.onDamageTaken(world, this, damage);
				
				double damageDone = damage.amount();
				//Double the damage done if it was a critical hit
				if(damage.isCrit())
				{
					damageDone *= 2;
				}
				
				//The player will be invincible for 750 ms after hit
				invincibilityTicks = 15; 
				
				//Determine the damage after armour, with a floor of 1 damage. If the damage penetrates armour
				//then this step is skipped
				if(!damage.penetratesArmor())
				{
					damageDone = MathHelper.floorOne(
						(damageDone * (1F - DEFENSE_REDUCTION_PERCENT * defense)) - (defense * DEFENSE_REDUCTION_FLAT)									
						);
				}
				
				//Apply absorbs if the damage is affected by them (IE it does not pierce them)
				if(!damage.piercesAbsorbs())
				{
					damageDone = dealDamageToAbsorbs(damageDone);
				}
				
				health -= damageDone; 
				//Show world text if applicable
				if(showWorldText)
				{
					String message = (damageDone == 0) ? "Absorb" : "" + (int)damageDone;
					world.addTemporaryText(message, (int)x - 2, (int)y - 3, 20, (damageDone == 0) ? EnumColor.WHITE : EnumColor.RED); 
				}
			}	

			//Put the player in combat if the damage causes combat status
			if(damage.causesCombatStatus())
			{
				putInCombat();
			}
		}		
	}
	
	/**
	 * Puts the player in combat.
	 */
	public void putInCombat()
	{
		isInCombat = true;
		ticksOfHealthRegen = 0;
	}
	
	/**
	 * Heals the player, and displays green WorldText if applicable.
	 * @param h the amount healed
	 * @param rendersText whether or not to render WorldText on the screen
	 */
	public boolean heal(World world, double h, boolean rendersText)
	{
		if(health < maxHealth)
		{
			health += h; 
			if(rendersText)
			{
				world.addTemporaryText(""+(int)h, (int)x - 2, (int)y - 3, 20, EnumColor.GREEN); //add temperary text to be rendered, for the healing done
			}
			if(health > maxHealth) //if health exceeds the maximum, set it to the maximum
			{
				health = maxHealth;
			}
			auraTracker.onHeal(world, this);
			return true;
		}
		return false;
	}
		
	/**
	 * Resets ticksSinceLastCast to 0, preventing mana regen for 6 seconds and restarting the regeneration cycle.
	 */
	public void resetCastTimer()
	{
		ticksSinceLastCast = 0;
	}
	
	/**
	 * Applies mana regen to the player, increasing as time between casts increases.
	 */
	private void applyManaRegen(World world)
	{
		if(ticksSinceLastCast > 120)
		{
			auraTracker.onManaRestored(world, this);
			mana += manaRegenerationModifier * (((ticksSinceLastCast - 120) / (1000 / 20) < (0.60 * 20)) ? ((ticksSinceLastCast - 120) / (1000 / 20)) : (0.60 * 20));
		}
		
		if(mana > maxMana)
		{
			mana = maxMana;
		}
	}
	
	/**
	 * Applies health regen that scales over time, until reaching the maximum of (11 HP/sec)
	 */
	private void applyHealthRegen(World world)
	{
		if(!isInCombat)
		{
			if(health < maxHealth)
			{
				auraTracker.onHeal(world, this);
			}
			double amountHealed = healthRegenerationModifier * ((ticksOfHealthRegen / (1800 / 20) < (20 * 0.55)) ? (ticksOfHealthRegen / (1800 / 20)) : (20 * 0.55));
			health += amountHealed;
			ticksOfHealthRegen++;	
		}
	
		if(health > maxHealth)
		{
			health = maxHealth;
		}
	}
	
	/**
	 * Recalculates defense and set bonuses when armour changes
	 */
	public void onArmorChange()
	{
		armorChanged = true;
		statsNeedResent = true;
	}
	
	public void onQuiverChange()
	{
		
	}
	
	/**
	 * Overrides EntityLiving onDeath() to provide special things like hardcore (mode) and itemdrops
	 */
	public void onDeath(World world)
	{	
		auraTracker.onDeath(world, this);
		
		//If the player is still dead, then trigger the respawn code
		if(health <= 0)
		{
			triggerDifficultyEffect(world);
		}
	}
	
	/**
	 * Triggers an effect appropriate to the player difficulty.
	 * @param world the world the player is in
	 */
	private void triggerDifficultyEffect(World world)
	{
		if(difficulty == EnumPlayerDifficulty.HARD)
		{
			dropAll(world);
		}
		else if(difficulty == EnumPlayerDifficulty.HARDCORE)
		{
			defeated = true;	
			return;
		}
		health = maxHealth;
		clearStatusEffects(world);
		world.spawnPlayer(TerraeRasa.terraeRasa.getSettings(), this);	
	}
	
	/**
	 * Drops all the player's inventory.
	 * @param world the world the player is in
	 */
	private void dropAll(World world)
	{
		Random random = new Random();
		for(int i = 0; i < inventory.getMainInventoryLength(); i++)
		{
			if(inventory.getMainInventoryStack(i) != null)
			{
				world.addItemStackToItemList((EntityItemStack)new EntityItemStack(x, 
						y - 15, 
						inventory.getMainInventoryStack(i)).setVelocity(new Vector2F((random.nextFloat() * 8) - 4, random.nextFloat() * 8)));
				inventory.removeEntireStackFromInventory(world, this, i);
			}
		}
		for(int i = 0; i < inventory.getArmorInventoryLength(); i++)
		{
			if(inventory.getArmorInventoryStack(i) != null)
			{
				world.addItemStackToItemList((EntityItemStack)new EntityItemStack(x, 
						y - 15, 
						inventory.getArmorInventoryStack(i)).setVelocity(new Vector2F((random.nextFloat() * 8) - 4, random.nextFloat() * 8)));
				inventory.setArmorInventoryStack(this, null, inventory.getArmorInventoryStack(i), i);			
			}
		}
		for(int i = 0; i < inventory.getQuiverLength(); i++)
		{
			if(inventory.getQuiverStack(i) != null)
			{
				world.addItemStackToItemList((EntityItemStack)new EntityItemStack(x, 
						y - 15, 
						inventory.getQuiverStack(i)).setVelocity(new Vector2F((random.nextFloat() * 8) - 4, random.nextFloat() * 8)));
				inventory.setQuiverStack(this, null, i);			
			}
		}
	}
	
	/**
	 * Flag recipes for recalculation
	 */
	public void onInventoryChange()
	{
		inventoryChanged = true; 
	}
	
	/**
	 * Serves for detecting what can be crafting, currently
	 */
	private void checkForNearbyBlocks(World world) 
	{
		nearBlock.put(Block.none.getName(), true);
		
	}
				
	/**
	 * Recalculates damage modifiers and health based on stats.
	 */
	public void recalculateStats()
	{
		rangeDamageModifier = 1.0 + (DAMAGE_BONUS_DEXTERITY * dexterity * dexterityModifier);
		meleeDamageModifier = 1.0 + (DAMAGE_BONUS_STRENGTH * strength * strengthModifier);
		magicDamageModifier = 1.0 + (DAMAGE_BONUS_INTELLECT * intellect * intellectModifier);
		maxHealth = (int) (temporaryMaxHealth + baseMaxHealth + (staminaModifier * stamina * HEALTH_FROM_STAMINA));
		maxMana = (int) (temporaryMaxMana + baseMaxMana + (intellectModifier * intellect * MANA_FROM_INTELLECT));	
		maxSpecialEnergy = temporarySpecialEnergy + baseSpecialEnergy;
		statsNeedResent = true;
	}
	
	/**
	 * Applies defense, stats, and PassiveBonuses from a single piece of armour that is being equipped. These 
	 * should be constant for a given piece of armour to allow for easy removing.
	 * @param armor the piece of armour being equipped
	 */
	public void applySingleArmorItem(ItemArmor armor, ItemStack stack, int index)
	{
		PassiveBonus[] bonuses = armor.getBonuses();		
		for(PassiveBonus bonus : bonuses)
		{
			bonus.apply(this);
		}		
		Vector<Aura> auras = new Vector<Aura>();
		for(GemSocket socket : stack.getGemSockets())
		{
			if(socket.getGem() != null)
			{
				for(PassiveBonus bonus : socket.getGem().getBonuses())
				{
					if(bonus != null)
					{
						bonus.apply(this);
					}
				}
				for(Aura aura : socket.getGem().getAuras())
				{
					if(aura != null)
					{
						auras.add(aura);
					}
				}
			}
		}
		for(PassiveBonus bonus : stack.getBonuses())
		{
			bonus.apply(this);
		}
		
		for(Aura aura : armor.getAuras())
		{
			auras.add(aura);
		}		
		for(Aura aura : stack.getAuras())
		{
			auras.add(aura);
		}
		
		auraTracker.setAurasByPiece(new AuraContainer(auras), index);			
		
		defense += armor.getDefense();
		dexterity += armor.getDexterity();
		intellect += armor.getIntellect();
		strength += armor.getStrength();
		stamina += armor.getStamina();
		armorChanged = true;
	}
	
	/**
	 * Removes defense, stats, and PassiveBonuses for a single piece of armour that is now being removed.
	 * These should be the same as when it was equipped due to constant values in armour that do not
	 * change.
	 * @param armor the piece of armour being removed
	 */
	public void removeSingleArmorItem(ItemArmor armor, ItemStack stack, int index)
	{
		PassiveBonus[] bonuses = armor.getBonuses();		
		for(PassiveBonus bonus : bonuses)
		{
			bonus.remove(this);
		}		
		for(GemSocket socket : stack.getGemSockets())
		{
			if(socket.getGem() != null)
			{
				for(PassiveBonus bonus : socket.getGem().getBonuses())
				{
					if(bonus != null)
					{
						bonus.remove(this);
					}
				}
			}
		}
		for(PassiveBonus bonus : stack.getBonuses())
		{
			bonus.remove(this);
		}
		
		auraTracker.setAurasByPiece(null, index);
		
		defense -= armor.getDefense();
		dexterity -= armor.getDexterity();
		intellect -= armor.getIntellect();
		strength -= armor.getStrength();
		stamina -= armor.getStamina();
		armorChanged = true;
	}
	
	/**
	 * Updates set bonus data. Unused bonuses are removed and new ones are applied. The new bonuses are 
	 * stored in the player's instance to be removed later.
	 */
	private void refreshPassiveBonuses()
	{
		if(currentBonuses != null)
		{
			currentBonuses.removeAll(this);
			currentBonuses = null;
		}
		currentBonuses = PassiveBonusFactory.getPassiveBonuses(inventory);
		currentBonuses.applyAll(this);
	}

	/**
	 * Function called when the player is mining a block
	 * @param mx x position in worldmap array, of the block being mined
	 * @param my y position in the worldmap array, of the block being mined
	 * @param item the tool mining the block
	 */
	public void breakBlock (ServerUpdate update, World world, int mx, int my, Item item)
	{
		Block block = world.getBlockGenerate(mx,  my);
		if(item == null)
		{
			return;
		}
		//Check If the left-mouse button is pressed && they have the correct tool to mine
		if (world.getBlock(mx, my).getID() != Block.air.getID()){
			if (((item instanceof ItemToolPickaxe && block.getBlockType() == 1) //TODO: Magic number fix @ block types
			  || (item instanceof ItemToolAxe && block.getBlockType() == 2) 
			  || (item instanceof ItemToolHammer && block.getBlockType() == 3) 
		      || block.getBlockType() == 0))
			{	
				EnumToolMaterial material;
				if (item instanceof ItemTool)
				{
					material = item.getToolMaterial();
				}
				else
				{
					material = EnumToolMaterial.FIST;
				}
				
				double distance = MathHelper.distanceBetweenTwoPoints(mx * 6, my * 6, (this.x + ((isFacingRight) ? 9 : 3)), (this.y + 9));
				if(distance <= material.getDistance())
				{
					if(material.getToolTier() >= block.getBlockTier()) //If the block is within range
					{ 	
						if(ticksreq == 0 || world.getBlock(mx, my) != world.getBlock(sx, sy)) //the mouse has moved or they arent mining anything
						{				
							isMining = false;
							sx = mx; //save the co-ords
							sy = my;
							ticksreq = (int) (block.getBlockHardness() / material.getStrength()) + 1; //Determine how long to break
						}	
						else if(ticksreq == 1 && block.getIsMineable()) //If the block should break
						{
							isMining = false;
							if(block.id == Block.cactus.getID()) 
							{
								world.breakCactus(update, this, mx, my);
							}
							else if(block.id == Block.tree.getID())
							{
								world.breakTree(update, this, mx, my);					
							}
							world.breakBlock(update, this, mx, my);
							
							//Overwrite snow/flowers/etc...
							if (world.getBlockGenerate(mx, my-1).getIsOveridable() == true && world.getBlock(mx, my-1).id != Block.air.getID())
							{
								world.breakBlock(update, this, mx, my-1);
							}
						}		
						else //mining is in progress, decrease remaining time.
						{
							isMining = true;
							ticksreq--;			
						}	
					}
				}
			}
		}
	}	
	
	/**
	 * Function called when the player is mining a back wall block
	 * @param mx x position in worldmap array, of the block being mined
	 * @param my y position in the worldmap array, of the block being mined
	 * @param item the tool mining the block
	 */
	public void breakBackBlock (ServerUpdate update, World world, int mx, int my, Item item)
	{
		if(item == null)
		{
			return;
		}
		if (world.getBackBlock(mx, my).getID() != Block.backAir.getID()){
			if (((item instanceof ItemToolPickaxe && world.getBackBlock(mx, my).getBlockType() == 1) 
			  || (item instanceof ItemToolAxe && world.getBackBlock(mx, my).getBlockType() == 2) 
			  || (item instanceof ItemToolHammer && world.getBackBlock(mx, my).getBlockType() == 3) 
		      || world.getBackBlock(mx, my).getBlockType() == 0))
			{ 
				//If the right-mouse button is pressed && they have the correct tool to mine
				EnumToolMaterial material;
				if (item instanceof ItemTool)
				{
					material = item.getToolMaterial();
				}
				else
				{
					material = EnumToolMaterial.FIST;
				}

				double distance = MathHelper.distanceBetweenTwoPoints(mx * 6, my * 6, (this.x + ((isFacingRight) ? 9 : 3)), (this.y + 9));
				if(distance <= material.getDistance() && 
						(!world.getBackBlock(mx, my-1).isSolid ||
						!world.getBackBlock(mx, my+1).isSolid || 
						!world.getBackBlock(mx - 1, my).isSolid ||
						!world.getBackBlock(mx + 1, my).isSolid))
				{
					if (material.getToolTier() >= world.getBackBlock(mx, my).getBlockTier()) //If the block is within range and at an edge
					{ 	
						if(ticksreq == 0 || world.getBackBlock(mx, my) != world.getBackBlock(sx, sy)) //the mouse has moved or they arent mining anything
						{				
							isMining = false;
							sx = mx; //save the co-ords
							sy = my;
							ticksreq = (int) (world.getBackBlock(mx, my).getBlockHardness() / material.getStrength()) + 1; //Determine how long to break
						}	
						else if(ticksreq == 1 && world.getBackBlock(mx,  my).getIsMineable()) //If the block should break
						{
							isMining = false;
							
							world.breakBackBlock(update, this, mx, my);
							
							//Overwrite snow/flowers/etc...
							if (world.getBackBlock(mx, my-1).getIsOveridable() == true && world.getBackBlock(mx, my-1) != Block.air)
							{
								world.breakBlock(update, this, mx, my-1);
							}
						}		
						else //mining is in progress, decrease remaining time.
						{
							isMining = true;
							ticksreq--;			
						}	
					}
				}
			}
		}
	}	
	
	/**
	 * Increases the maximum health of the player
	 * @param increase the amount to increase maxHealth
	 * @return success of the operation
	 */
	public boolean boostMaxHealth(int increase) 
	{
		if(baseMaxHealth + increase <= MAXIMUM_BASE_HEALTH)//if the player can have that much of a max health increase
		{
			maxHealth += increase; //increase max health
			baseMaxHealth += increase;
			return true;
		}
		return false;
	}
	
	/**
	 * Increases the maximum mana of the player
	 * @param increase the amount to increase maxMana
	 * @return success of the operation
	 */
	public boolean boostMaxMana(int increase) 
	{
		if(baseMaxMana + increase <= MAXIMUM_BASE_MANA) //if the player can have that much of a max mana increase
		{
			baseMaxMana += increase;
			maxMana += increase; //increase max mana
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the player's difficulty setting. This field is final, and unchanging
	 * @return the player's difficulty setting
	 */
	public final EnumPlayerDifficulty getDifficulty()
	{
		return difficulty;
	}
	
	/**
	 * Gets whether or not the player is currently mining a block
	 * @return whether or not the player is currently mining
	 */
	public boolean getIsMining(){
		return isMining;
	}
	
	/**
	 * Gets the maximum distance the player can place a block, measured in ortho units. this field is final
	 * @return MAX_BLOCK_PLACE_DISTANCE field
	 */
	public final int getMaximumBlockPlaceDistance()
	{
		return MAX_BLOCK_PLACE_DISTANCE;
	}
	
	/**
	 * Sets the position of the viewed chest (from 'world map'), and changes isViewingChest to true.
	 * @param x the x position of the chest, in the 'world map'
	 * @param y the y position of the chest, in the 'world map'
	 */
	public void setViewedChest(int x, int y)
	{
		isViewingChest = true;
		viewedChestX = x;
		viewedChestY = y;
	}
	
	/**
	 * Clears the currently viewed chest position to 0,0, and sets isViewingChest to false
	 */
	public void clearViewedChest()
	{
		isViewingChest = false;
		viewedChestX = -1;
		viewedChestY = -1;
	}
	
	/**
	 * Resets all variables relating to mining, to a default value so the player can mine again
	 */
	public void resetMiningVariables()
	{
		ticksreq = 0;
		sy = 0;
		sx = 0;
	}
	
	/**
	 * Launch a player spell projectile
	 * @param world = current world
	 * @param mouseX = x position to create the projectile at
	 * @param mouseY = y position to create the projectile at
	 * @param item = Item to be used to launch projectile (what projectile is needed)
	 */
	public void launchProjectileMagic(ServerUpdate update, World world, double mouseX, double mouseY, ItemMagic item){		
		if (mana >= item.getManaReq()){
			int angle = MathHelper.angleMousePlayer(mouseX, mouseY, x, y);
			if (angle < 0){
				angle += 360;
			}
			EntityProjectile projectile = new EntityProjectile(item.getProjectile()).setXLocAndYLoc(x, y)
					.setDirection(angle).setDamage(item.getProjectile().getDamage() * rangeDamageModifier * allDamageModifier);
			EntityUpdate entityUpdate = new EntityUpdate();
			entityUpdate.action = 'a';
			entityUpdate.type = 4;
			entityUpdate.entityID = projectile.entityID;
			entityUpdate.updatedEntity = projectile;
			update.addEntityUpdate(entityUpdate);
			world.addEntityToProjectileList(projectile);
			mana -= item.getManaReq();			
		}		
	}

	/**
	 * Launch a player weapon projectile
	 * @param world = current world
	 * @param mouseX = x position to create the projectile at
	 * @param mouseY = y position to create the projectile at
	 * @param item = Item to be used to launch projectile (to determine what projectile is needed)
	 */
	public void launchProjectileWeapon(ServerUpdate update, World world, double mouseX, double mouseY, ItemRanged item){
		if (ticksSinceLastCast > item.getCooldownTicks())
		{
			for (int i = 0; i < inventory.getQuiverLength(); i++)
			{			
				if(inventory.getQuiverStack(i) != null)
				{	
					ItemAmmo ammunition = (ItemAmmo) Item.itemsList[inventory.getQuiverStack(i).getItemID()];
					
					int angle = MathHelper.angleMousePlayer(mouseX, mouseY, x, y) - 90;
					if (angle < 0)
					{
						angle += 360;
					}
					
					EntityProjectile projectile = new EntityProjectile(ammunition.getProjectile()).setDrop(new ItemStack(ammunition)).setXLocAndYLoc(x, y)
							.setDirection(angle).setDamage((ammunition.getProjectile().getDamage() + item.getDamage()) * rangeDamageModifier * allDamageModifier);
					EntityUpdate entityUpdate = new EntityUpdate();
					entityUpdate.action = 'a';
					entityUpdate.type = 4;
					entityUpdate.entityID = projectile.entityID;
					entityUpdate.updatedEntity = projectile;
					update.addEntityUpdate(entityUpdate);
					world.addEntityToProjectileList(projectile);
								
					String command = "/player " + entityID + " quiverremove " + i + " " + 1;
					update.addValue(command);					
					inventory.removeItemsFromQuiverStack(this, 1, i);
					ticksSinceLastCast = 0;
					break;
				}
			}
		}
	}
	
	/**
	 * Launch a player weapon projectile
	 * @param world = current world
	 * @param mouseX = x position to create the projectile at
	 * @param mouseY = y position to create the projectile at
	 * @param item = Item to be used to launch projectile (to determine what projectile is needed)
	 */
	public void launchProjectileThrown(ServerUpdate update, World world, double mouseX, double mouseY, ItemThrown item, int index){
		if (ticksSinceLastCast > item.getCooldownTicks())
		{
			int angle = MathHelper.angleMousePlayer(mouseX, mouseY, x, y) - 90;
			if (angle < 0)
			{
				angle += 360;
			}
						
			EntityProjectile projectile = new EntityProjectile(item.getProjectile()).setDrop(new ItemStack(item)).setXLocAndYLoc(x, y)
					.setDirection(angle).setDamage(item.getProjectile().getDamage() * rangeDamageModifier * allDamageModifier);
			EntityUpdate entityUpdate = new EntityUpdate();
			entityUpdate.action = 'a';
			entityUpdate.type = 4;
			entityUpdate.entityID = projectile.entityID;
			entityUpdate.updatedEntity = projectile;
			update.addEntityUpdate(entityUpdate);
			world.addEntityToProjectileList(projectile);				
			
			
			String command = "/player " + entityID + " inventoryremove " + index + " " + 1;
			update.addValue(command);			
			inventory.removeItemsFromInventoryStack(this, 1, index);
			ticksSinceLastCast = 0;
		}
	}
	
	/**
	 * Gets the Block the player is holding, if it's a Block and it's an instanceof BlockLight. Used for handheld lighting.
	 * @return an instanceof BlockLight if the player is holding one, otherwise null
	 */
	public Block getHandheldLight()
	{
		ItemStack stack = inventory.getMainInventoryStack(selectedSlot);
		if(stack != null)
		{
			if(stack.getItemID() < Item.itemIndex && (Block.blocksList[stack.getItemID()]).lightStrength > 0)
			{
				return Block.blocksList[stack.getItemID()];
			}
		}
		return null;
	}
	
	/**
	 * Decreases the ticksLeft on each cooldown by 1, and removes that cooldown if it's expired.
	 */
	private void updateCooldowns()
	{
		Enumeration<String> keys = cooldowns.keys();
		while (keys.hasMoreElements()) 
		{
			String str = (String) keys.nextElement();
			Cooldown cooldown = cooldowns.get(str);
			cooldown.ticksLeft--;
			if(cooldown.ticksLeft <= 0)
			{
				cooldowns.remove(str);
			}
		}
	}
	
	/**
	 * Puts an ID on cooldown, which generally relates to an item or something else. If that ID is already on cooldown and
	 * with a longer time, this will fail and the current cooldown will continue, otherwise the new cooldown will be implemented.
	 * @param id the id to put on cooldown
	 * @param duration the duration of the cooldown in game ticks
	 */
	public void putOnCooldown(int id, int duration)
	{
		if(cooldowns.get("" + id) != null)
		{
			Cooldown current = cooldowns.get("" + id);
			if(current.ticksLeft < duration)
			{
				cooldowns.put("" + id, new Cooldown(id, duration));
			}		
			return;	
		}
		cooldowns.put("" + id, new Cooldown(id, duration));
	}
	
	/**
	 * Gets whether or not an ID is on cooldown. This does not verify that ID actually exists.
	 * @param id the id that should be checked for a cooldown
	 * @return if the ID is on cooldown (true if it is, false otherwise)
	 */
	public boolean isOnCooldown(int id)
	{
		return cooldowns.get("" + id) != null;
	}
	
	/**
	 * Gets the time remaining on an ID's cooldown. This does not verify that ID actually exists.
	 * @param id the id that should be queried for a cooldown remaining
	 * @return the time left on the specified ID's cooldown, in ticks
	 */
	public int getTicksLeftOnCooldown(int id)
	{
		return cooldowns.get("" + id).ticksLeft;
	}

	/**
	 * Regens a little bit of the special energy bar, totalling 25% per minute * applicable modifiers
	 */
	private void applySpecialRegen(World world)
	{
		//25% per minute.
		specialEnergy += (0.020833333 * 20.0) * specialRegenerationModifier;		
		if(specialEnergy > maxSpecialEnergy)
		{
			specialEnergy = maxSpecialEnergy;
		}
	}
	
	/**
	 * Restores special energy, upto the cap
	 * @param restore the amount restored
	 * @return whether or not any special energy was restored
	 */
	public boolean restoreSpecialEnergy(int restore)
	{
		if(specialEnergy == maxSpecialEnergy)
		{
			return false;
		}
		
		specialEnergy += restore;
		if(specialEnergy > maxSpecialEnergy)
		{
			specialEnergy = maxSpecialEnergy;
		}
		return true;
	}
	
	/**
	 * Spends the given amount of special energy - even if the player doesnt have it.
	 * @param amount the amount of energy to spend
	 */
	public void spendSpecialEnergy(int amount)
	{
		specialEnergy -= amount;
	}
	
	public double getToolRotationAngle()
	{
		return rotateAngle;
	}
	
	public boolean isSwingingTool()
	{
		return hasSwungTool;
	}
	
	public boolean getIsSwingingRight()
	{
		return isSwingingRight;
	}
	
	/**
	 * Adds the given radian value to the tool rotation angle. If this is negative, it will
	 * be subtracted.
	 * @param addition the amount to add to the tool rotation angle, in radians
	 */
	public void updateRotationAngle(double addition)
	{
		rotateAngle += addition;
	}
	
	/**
	 * Stops the swinging of the player weapon
	 */
	public void clearSwing()
	{
		hasSwungTool = false;
	}
	
	/**
	 * Updates the tool swing. This is based on a weapon's swing speed - a faster speed swings faster.
	 */
	public void updateSwing(ServerUpdate update)
	{
		double swingSpeed = (double)((ItemTool)(Item.itemsList[inventory.getMainInventoryStack(selectedSlot).getItemID()])).swingTime;
		swingSpeed = (swingSpeed / attackSpeedModifier);
		if(isSwingingRight)
		{
            rotateAngle += MathHelper.degreeToRadian((double)totalSwingArcDeg / (swingSpeed * 20));	        
            if(rotateAngle <= leftSwingBound || rotateAngle >= rightSwingBound )
	        {
	        	clearSwing();
	        	String command = "/player " + entityID + " stopswing";
				update.addValue(command);
	        }
            else
            {
            	String command = "/player " + entityID + " swingangle " + getSwingAngle();
				update.addValue(command);
            }
		}
		else
		{
			rotateAngle -= MathHelper.degreeToRadian((double)totalSwingArcDeg / (swingSpeed * 20));
		    if(rotateAngle <= leftSwingBound || rotateAngle >= rightSwingBound)
	        {
	        	clearSwing();
		    	String command = "/player " + entityID + " stopswing";
				update.addValue(command);
	        }
		    else
		    {
		    	String command = "/player " + entityID + " swingangle " + getSwingAngle();
				update.addValue(command);
		    }
		}
	}
	
	/**
	 * Starts swinging the tool based on direction, resetting the angle to an appropriate starting location. 
	 * @param swingRight whether the swing is right, or not (IE left)
	 */
	public void startSwingingTool()
	{
		isSwingingRight = isFacingRight;
		hasSwungTool = true;
		if(isSwingingRight)
		{
			rotateAngle = leftSwingBound;			
		}
		else
		{
			rotateAngle = rightSwingBound;
		}
	}
	
	/**
	 * If the player deals damage to a monster or something else this should be called. This will cause
	 * auras to fire their onDamageDone() events.
	 */
	public void inflictedDamageToMonster(World world, Damage damage) 
	{
		auraTracker.onDamageDone(world, this, damage);
	}

	/**
	 * Gets the player's health percentage, from 0.0-1.0, where 0.0 indicates 0% and 1.0 indicates 100%
	 * @return the player's health percent as a double between 0.0 and 1.0
	 */
	public double getHealthPercent() 
	{
		return health / maxHealth;
	}
	
	/**
	 * Gets the player's mana percentage, from 0.0-1.0, where 0.0 indicates 0% and 1.0 indicates 100%.
	 * If the player has no mana, this will yield a value of 0.0
	 * @return the player's mana percent as a double between 0.0 and 1.0
	 */
	public double getManaPercent() 
	{
		return (maxMana == 0) ? 0 : mana / maxMana;
	}
	
	public static CompressedPlayer compress(EntityPlayer player)
	{
		CompressedPlayer compressPlayer = new CompressedPlayer();
		compressPlayer.entityID = player.entityID;
		compressPlayer.x = player.x;
		compressPlayer.y = player.y;
		compressPlayer.isAffectedByWalls = player.isAffectedByWalls;
		compressPlayer.isAffectedByGravity = player.isAffectedByGravity;
		compressPlayer.upwardJumpCounter = player.upwardJumpCounter; 	
		compressPlayer.canJumpAgain = player.canJumpAgain;
		compressPlayer.isJumping = player.isJumping;	
		compressPlayer.upwardJumpHeight = player.upwardJumpHeight; 
		compressPlayer.jumpSpeed = player.jumpSpeed;	
		compressPlayer.isStunned = player.isStunned;
		compressPlayer.ticksFallen = player.ticksFallen;
		compressPlayer.textureWidth = player.textureWidth;
		compressPlayer.textureHeight = player.textureHeight;
		compressPlayer.width = player.width;
		compressPlayer.height = player.height;
		compressPlayer.blockWidth = player.blockWidth;
		compressPlayer.blockHeight = player.blockHeight;
		compressPlayer.distanceFallen = player.distanceFallen;
		compressPlayer.maxHeightFallenSafely = player.maxHeightFallenSafely;
		compressPlayer.baseSpeed = player.baseSpeed;
		compressPlayer.movementSpeedModifier = player.movementSpeedModifier;

		compressPlayer.attackSpeedModifier = player.attackSpeedModifier;
		compressPlayer.knockbackModifier = player.knockbackModifier;
		compressPlayer.meleeDamageModifier = player.meleeDamageModifier;
		compressPlayer.rangeDamageModifier = player.rangeDamageModifier;
		compressPlayer.magicDamageModifier = player.magicDamageModifier;
		compressPlayer.allDamageModifier = player.allDamageModifier;	
		compressPlayer.statusEffects = player.statusEffects;	
		compressPlayer.criticalStrikeChance = player.criticalStrikeChance; 
		compressPlayer.dodgeChance = player.dodgeChance;
		compressPlayer.isImmuneToFallDamage = player.isImmuneToFallDamage;
		compressPlayer.invincibilityTicks = player.invincibilityTicks;	
		compressPlayer.maxHealth = player.maxHealth;
		compressPlayer.maxMana = player.maxMana;
		compressPlayer.mana = player.mana;
		compressPlayer.defense = player.defense;
		compressPlayer.health = player.health;
		compressPlayer.absorbs = player.absorbs;
		
		compressPlayer.isSwingingRight = player.isSwingingRight;
		compressPlayer.hasSwungTool = player.hasSwungTool;
		compressPlayer.rotateAngle = player.rotateAngle;
		compressPlayer.armorChanged = player.armorChanged;
		compressPlayer.ticksSinceLastCast = player.ticksSinceLastCast;
		compressPlayer.ticksInCombat = player.ticksInCombat;
		compressPlayer.ticksOfHealthRegen = player.ticksOfHealthRegen;
		compressPlayer.isInCombat = player.isInCombat;
		compressPlayer.isMining = player.isMining;
		compressPlayer.isReloaded = player.isReloaded;
		compressPlayer.ticksreq = player.ticksreq;
		compressPlayer.sx = player.sx;
		compressPlayer.sy = player.sy;		
		compressPlayer.playerName = player.playerName;
		compressPlayer.inventoryChanged = player.inventoryChanged;
		compressPlayer.difficulty = player.difficulty;

		compressPlayer.cooldowns = player.cooldowns;
		compressPlayer.baseSpecialEnergy = player.baseSpecialEnergy;
		compressPlayer.nearBlock = player.nearBlock;
		compressPlayer.currentBonuses = player.currentBonuses; 
		compressPlayer.auraTracker = player.auraTracker;
		
		compressPlayer.strength = player.strength;
		compressPlayer.dexterity = player.dexterity;
		compressPlayer.intellect = player.intellect;
		compressPlayer.stamina = player.stamina;
		
		compressPlayer.temporarySpecialEnergy = player.temporarySpecialEnergy;
		compressPlayer.specialEnergy = player.specialEnergy;
		compressPlayer.maxSpecialEnergy = player.maxSpecialEnergy;
		
		compressPlayer.viewedChestX = player.viewedChestX;
		compressPlayer.viewedChestY = player.viewedChestY;
		compressPlayer.isViewingChest = player.isViewingChest;	
		compressPlayer.baseMaxHealth = player.baseMaxHealth;
		compressPlayer.temporaryMaxHealth = player.temporaryMaxHealth;
		compressPlayer.baseMaxMana = player.baseMaxMana;	
		compressPlayer.temporaryMaxMana = player.temporaryMaxMana;
		compressPlayer.respawnXPos = player.respawnXPos;
		compressPlayer.respawnYPos = player.respawnYPos;	
		
		compressPlayer.selectedRecipe = player.selectedRecipe;
		compressPlayer.selectedSlot = player.selectedSlot;
		compressPlayer.isFacingRight = player.isFacingRight;
		compressPlayer.isInventoryOpen = player.isInventoryOpen;	
		compressPlayer.inventory = player.inventory;
		
		compressPlayer.healthRegenerationModifier = player.healthRegenerationModifier;
		compressPlayer.manaRegenerationModifier = player.manaRegenerationModifier;
		compressPlayer.specialRegenerationModifier = player.specialRegenerationModifier;
		
		compressPlayer.pickupRangeModifier = player.pickupRangeModifier;
		compressPlayer.staminaModifier = player.staminaModifier;
		compressPlayer.intellectModifier = player.intellectModifier;
		compressPlayer.dexterityModifier = player.dexterityModifier;
		compressPlayer.strengthModifier = player.strengthModifier;
		
		compressPlayer.defeated = player.defeated;
		
		
		
		return compressPlayer;
	}
	
	public static EntityPlayer expand(CompressedPlayer compressedPlayer)
	{
		EntityPlayer player = new EntityPlayer(compressedPlayer.playerName, compressedPlayer.difficulty);
		player.entityID = compressedPlayer.entityID;
		player.x = compressedPlayer.x;
		player.y = compressedPlayer.y;
		player.isAffectedByWalls = compressedPlayer.isAffectedByWalls;
		player.isAffectedByGravity = compressedPlayer.isAffectedByGravity;
		player.upwardJumpCounter = compressedPlayer.upwardJumpCounter; 	
		player.canJumpAgain = compressedPlayer.canJumpAgain;
		player.isJumping = compressedPlayer.isJumping;	
		player.upwardJumpHeight = compressedPlayer.upwardJumpHeight; 
		player.jumpSpeed = compressedPlayer.jumpSpeed;	
		player.isStunned = compressedPlayer.isStunned;
		player.ticksFallen = compressedPlayer.ticksFallen;
		player.textureWidth = compressedPlayer.textureWidth;
		player.textureHeight = compressedPlayer.textureHeight;
		player.width = compressedPlayer.width;
		player.height = compressedPlayer.height;
		player.blockWidth = compressedPlayer.blockWidth;
		player.blockHeight = compressedPlayer.blockHeight;
		player.distanceFallen = compressedPlayer.distanceFallen;
		player.maxHeightFallenSafely = compressedPlayer.maxHeightFallenSafely;
		player.baseSpeed = compressedPlayer.baseSpeed;
		player.movementSpeedModifier = compressedPlayer.movementSpeedModifier;

		player.attackSpeedModifier = compressedPlayer.attackSpeedModifier;
		player.knockbackModifier = compressedPlayer.knockbackModifier;
		player.meleeDamageModifier = compressedPlayer.meleeDamageModifier;
		player.rangeDamageModifier = compressedPlayer.rangeDamageModifier;
		player.magicDamageModifier = compressedPlayer.magicDamageModifier;
		player.allDamageModifier = compressedPlayer.allDamageModifier;	
		player.statusEffects = compressedPlayer.statusEffects;	
		player.criticalStrikeChance = compressedPlayer.criticalStrikeChance; 
		player.dodgeChance = compressedPlayer.dodgeChance;
		player.isImmuneToFallDamage = compressedPlayer.isImmuneToFallDamage;
		player.invincibilityTicks = compressedPlayer.invincibilityTicks;	
		player.maxHealth = compressedPlayer.maxHealth;
		player.maxMana = compressedPlayer.maxMana;
		player.mana = compressedPlayer.mana;
		player.defense = compressedPlayer.defense;
		player.health = compressedPlayer.health;
		player.absorbs = compressedPlayer.absorbs;
		
		player.isSwingingRight = compressedPlayer.isSwingingRight;
		player.hasSwungTool = compressedPlayer.hasSwungTool;
		player.rotateAngle = compressedPlayer.rotateAngle;
		player.armorChanged = compressedPlayer.armorChanged;
		player.ticksSinceLastCast = compressedPlayer.ticksSinceLastCast;
		player.ticksInCombat = compressedPlayer.ticksInCombat;
		player.ticksOfHealthRegen = compressedPlayer.ticksOfHealthRegen;
		player.isInCombat = compressedPlayer.isInCombat;
		player.isMining = compressedPlayer.isMining;
		player.isReloaded = compressedPlayer.isReloaded;
		player.ticksreq = compressedPlayer.ticksreq;
		player.sx = compressedPlayer.sx;
		player.sy = compressedPlayer.sy;		
		//player.playerName = ;
		player.inventoryChanged = compressedPlayer.inventoryChanged;
		//player.difficulty = ;

		player.cooldowns = compressedPlayer.cooldowns;
		player.baseSpecialEnergy = compressedPlayer.baseSpecialEnergy;
		player.nearBlock = compressedPlayer.nearBlock;
		player.currentBonuses = compressedPlayer.currentBonuses; 
		player.auraTracker = compressedPlayer.auraTracker;
		
		player.strength = compressedPlayer.strength;
		player.dexterity = compressedPlayer.dexterity;
		player.intellect = compressedPlayer.intellect;
		player.stamina = compressedPlayer.stamina;
		
		player.temporarySpecialEnergy = compressedPlayer.temporarySpecialEnergy;
		player.specialEnergy = compressedPlayer.specialEnergy;
		player.maxSpecialEnergy = compressedPlayer.maxSpecialEnergy;
		
		player.viewedChestX = compressedPlayer.viewedChestX;
		player.viewedChestY = compressedPlayer.viewedChestY;
		player.isViewingChest = compressedPlayer.isViewingChest;	
		player.baseMaxHealth = compressedPlayer.baseMaxHealth;
		player.temporaryMaxHealth = compressedPlayer.temporaryMaxHealth;
		player.baseMaxMana = compressedPlayer.baseMaxMana;	
		player.temporaryMaxMana = compressedPlayer.temporaryMaxMana;
		player.respawnXPos = compressedPlayer.respawnXPos;
		player.respawnYPos = compressedPlayer.respawnYPos;	
		
		player.selectedRecipe = compressedPlayer.selectedRecipe;
		player.selectedSlot = compressedPlayer.selectedSlot;
		player.isFacingRight = compressedPlayer.isFacingRight;
		player.isInventoryOpen = compressedPlayer.isInventoryOpen;	
		player.inventory = compressedPlayer.inventory;
		
		player.healthRegenerationModifier = compressedPlayer.healthRegenerationModifier;
		player.manaRegenerationModifier = compressedPlayer.manaRegenerationModifier;
		player.specialRegenerationModifier = compressedPlayer.specialRegenerationModifier;
		
		player.pickupRangeModifier = compressedPlayer.pickupRangeModifier;
		player.staminaModifier = compressedPlayer.staminaModifier;
		player.intellectModifier = compressedPlayer.intellectModifier;
		player.dexterityModifier = compressedPlayer.dexterityModifier;
		player.strengthModifier = compressedPlayer.strengthModifier;
		
		player.defeated = compressedPlayer.defeated;
		
		return player;
	}
	
	public StatUpdate getStats()
	{
		StatUpdate update = new StatUpdate();
		update.entityID = this.entityID;
		update.isStunned = this.isStunned;
		update.defense = this.defense;
		update.mana = this.mana;
		update.maxMana = this.maxMana;
		update.health = this.health;
		update.maxHealth = this.maxHealth;
		update.specialEnergy = this.specialEnergy;
		update.maxSpecialEnergy = this.maxSpecialEnergy;
		update.isSwingingRight = this.isSwingingRight;
		update.hasSwungTool = this.hasSwungTool;
		update.rotateAngle = this.rotateAngle;
		update.statusEffects = this.statusEffects;
		update.absorbs = this.absorbs;
		update.cooldowns = this.cooldowns;
		update.defeated = this.defeated;
		return update;
		
	}
	
	public void setStats(StatUpdate newStats)
	{
		this.isStunned = newStats.isStunned;
		this.defense = newStats.defense;
		this.mana = newStats.mana;
		this.maxMana = (int) newStats.maxMana;
		this.health = newStats.health;
		this.maxHealth = (int) newStats.maxHealth;
		this.specialEnergy = newStats.specialEnergy;
		this.maxSpecialEnergy = newStats.maxSpecialEnergy;
		this.isSwingingRight = newStats.isSwingingRight;
		this.hasSwungTool = newStats.hasSwungTool;
		this.rotateAngle = newStats.rotateAngle;
		this.statusEffects = newStats.statusEffects;
		this.absorbs = newStats.absorbs;
		this.cooldowns = newStats.cooldowns;
		this.defeated = newStats.defeated;
	}

	public double getSwingAngle() 
	{
		return rotateAngle;
	}
	
}